import exceptions.{IllegalAmountException, NoSufficientFundsException}

import java.util.concurrent.ForkJoinPool

class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private var uidCounter = 0
    private val executor = new ForkJoinPool

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        val transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
        this.transactionsQueue.push(transaction)
        executor.execute(new Runnable {
            override def run(): Unit = processTransactions
        })
    }

    private def processTransactions: Unit = {
        var isEmpty: Boolean = false
        var transaction: Transaction = null
        // using synchronized for thread safety
        transactionsQueue.synchronized {
            isEmpty = transactionsQueue.isEmpty
            if(!isEmpty) {
                transaction = transactionsQueue.pop;
            }
        }
        // if not empty, the first element from queue was popped
        if(!isEmpty) {
            try {
                transaction.run()
                // if the transaction doesn't throw an exception, it was successful
                transaction.status = TransactionStatus.SUCCESS
            } catch {
                // if it does throw and exception, handle it
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
                // if the transaction either failed or succeeded, add it to the processed transactions
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
