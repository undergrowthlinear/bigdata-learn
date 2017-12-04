package moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-25-23:20
  * @version 1.0.0
  */
object StateObjectDemo {
  def main(args: Array[String]) {
    val bankAccount = new BankAccount
    bankAccount.deposit(100)
    println(bankAccount.balance())
    println(bankAccount.withdraw(80))
    println(bankAccount.withdraw(80))
  }
}

// 有状态的对象
// 对象的每个非私有var类型成员变量都隐含getter(成员变量名称) 与 setter(成员变量_=)
class BankAccount {
  private var bal: Int = 0

  def balance(): Int = bal

  def deposit(amount: Int): Unit = {
    require(amount > 0)
    bal += amount
  }

  def withdraw(amount: Int): Boolean = {
    if (amount > bal) return false
    else {
      bal -= amount
      true
    }
  }

}
