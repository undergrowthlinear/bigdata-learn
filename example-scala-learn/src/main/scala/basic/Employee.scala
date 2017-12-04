package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-13:47
  * @version 1.0.0
  */
class Employee extends Person {
  var salary = 0.0

  override def toString = super.toString + "[salary=" + salary + "]"
}
