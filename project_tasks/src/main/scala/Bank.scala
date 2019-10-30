import exceptions.{IllegalAmountException, NoSufficientFundsException}

import java.util.concurrent.ForkJoinPool

class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private var uidCounter = 0
    private val executor = new ForkJoinPool


    // TODO
    // project task 2
    // create a new transaction object and put it in the queue
    // spawn a thread that calls processTransactions
    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        var transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
        this.transactionsQueue.push(transaction)
        executor.execute(new Runnable {
            override def run(): Unit = processTransactions
        })
    }

    // TODO
    // project task 2
    // Function that pops a transaction from the queue
    // and spawns a thread to execute the transaction.
    // Finally do the appropriate thing, depending on whether
    // the transaction succeeded or not
    private def processTransactions: Unit = {
        // **************************
        // TODO: Sometimes transactionsQueue.pop fails because the queue is empty
        // because another thread has poped the queue after we used isEmpty
        // so for now i synchronized transactionsQueue but is this inefficient?
        // is there a better way to do it??????
        // **************************
        var isEmpty: Boolean = false
        var transaction: Transaction = null
        transactionsQueue.synchronized {
            isEmpty = transactionsQueue.isEmpty
            if(!isEmpty) {
                transaction = transactionsQueue.pop;
            }
        }

        if(!isEmpty) {
            try {
                transaction.run()
                transaction.status = TransactionStatus.SUCCESS
            } catch {
                case illegalAmount: IllegalAmountException => {
                    transaction.status = TransactionStatus.FAILED
                }
                case noSufficientFunds: NoSufficientFundsException => {
                    transaction.attempt += 1
                    if (transaction.attempt >= transaction.allowedAttempts) {
                        transaction.status = TransactionStatus.FAILED
                    } 
                    else {
                        this.transactionsQueue.push(transaction)
                    }
                }
            } finally {
                if (transaction.status != TransactionStatus.PENDING) {
                    this.processedTransactions.push(transaction)
                }  
            }
            executor.execute(new Runnable {
                override def run(): Unit = processTransactions
            })
        }
        
    }

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

    def generateUID: Int = this.synchronized {
        uidCounter += 1
        uidCounter
    }

}
