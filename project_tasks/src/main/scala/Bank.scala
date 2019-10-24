import exceptions.{IllegalAmountException, NoSufficientFundsException}

class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
        var transaction = new Transaction(transactionsQueue, processedTransactions, from, to, amount, 1)
        this.processedTransactions.push(transaction)
        this.processTransactions
    }
                                                // TODO
                                                // project task 2
                                                // create a new transaction object and put it in the queue
                                                // spawn a thread that calls processTransactions

    private def processTransactions: Unit = {
        if(!transactionsQueue.isEmpty) {
            var next = transactionsQueue.pop
            try {
                next.run()
                next.status = TransactionStatus.SUCCESS
            } catch {
                case illegalAmount: IllegalAmountException => {
                    next.status = TransactionStatus.FAILED
                    this.processedTransactions.push(next)
                }
                case noSufficientFunds: NoSufficientFundsException => {
                    if (next.attempt == next.allowedAttempts) {
                        next.status = TransactionStatus.FAILED
                    }
                    else {
                        this.transactionsQueue.push(next)
                    }
                }
            } finally {
                next.attempt += 1
                if (next.status != TransactionStatus.PENDING) {
                    this.processedTransactions.push(next)
                }  
            }
            this.processTransactions
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

}
