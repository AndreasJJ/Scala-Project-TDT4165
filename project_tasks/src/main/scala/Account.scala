import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

    class Balance(var amount: Double) {}

    val balance = new Balance(initialBalance)
    val uid = bank.generateUID

    def withdraw(amount: Double): Either[Unit,String] = this.synchronized {
        if(amount <= 0) {
            Right("The amount should be more than 0")
        }
        else if(this.balance.amount - amount >= 0) {
            this.balance.amount -= amount
            Left((): Unit)
        } else {
            Right("Not enough funds to withdraw amount")
        }
    }
    
    def deposit (amount: Double): Either[Unit,String] = this.synchronized {
        if(amount <= 0) {
            Right("The amount should be more than 0")
        } else {
            this.balance.amount += amount
            Left((): Unit)
        }
    }

    def getBalanceAmount: Double = this.balance.amount

    def transferTo(account: Account, amount: Double) = {
        bank addTransactionToQueue (this, account, amount)
    }
}
