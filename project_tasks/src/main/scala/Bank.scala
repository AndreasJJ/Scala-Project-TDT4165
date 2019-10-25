import exceptions.{IllegalAmountException, NoSufficientFundsException}

class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private var uidCounter = 0

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        var transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
        this.transactionsQueue.push(transaction)
        this.processTransactions
    }
                                                // TODO
                                                // project task 2
                                                // create a new transaction object and put it in the queue
                                                // spawn a thread that calls processTransactions

    private def processTransactions: Unit = {
        // **************************
        // TODO: Sometimes transactionsQueue.pop fails because the queue is empty
        // because another thread has poped the queue after we used isEmpty
        // so for now i synchronized transactionsQueue but is this inefficient?
        // is there a better way to do it??????
        // **************************
        transactionsQueue.synchronized {
        if(!transactionsQueue.isEmpty) {
            var next = transactionsQueue.pop
            try {
                next.run()
                next.status = TransactionStatus.SUCCESS
            } catch {
                case illegalAmount: IllegalAmountException => {
                    next.status = TransactionStatus.FAILED
                }
                case noSufficientFunds: NoSufficientFundsException => {
                    next.attempt += 1
                    if (next.attempt >= next.allowedAttempts) {
                        next.status = TransactionStatus.FAILED
                    } 
                    else {
                        this.transactionsQueue.push(next)
                    }
                }
            } finally {
                if (next.status != TransactionStatus.PENDING) {
                    this.processedTransactions.push(next)
                }  
            }
            this.processTransactions
        }
        }
    }
                                                // TOO
                                                // project task 2
                                                // Function that pops a transaction from the queue
                                                // and spawns a thread to execute the transaction.
                                                // Finally do the appropriate thing, depending on whether
                                                // the transaction succeeded or not

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
