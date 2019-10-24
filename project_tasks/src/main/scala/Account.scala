import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)

    // TODO
    // for project task 1.2: implement functions
    // for project task 1.3: change return type and update function bodies
    def withdraw(amount: Double): Either[Unit,String] = this.synchronized {
        if(amount <= 0) {
            //throw new exceptions.IllegalAmountException()
            Right("The amount should be more than 0")
        }
        else if(this.balance.amount - amount >= 0) {
            this.balance.amount -= amount
            Left((): Unit)
        } else {
            //throw new exceptions.NoSufficientFundsException()
            Right("Not enough funds to withdraw amount")
        }
    }
    def deposit (amount: Double): Either[Unit,String] = this.synchronized {
        if(amount <= 0) {
            //throw new exceptions.IllegalAmountException()
            Right("The amount should be more than 0")
        } else {
            this.balance.amount += amount
            Left((): Unit)
        }
    }
    def getBalanceAmount: Double       = this.balance.amount

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }


}
