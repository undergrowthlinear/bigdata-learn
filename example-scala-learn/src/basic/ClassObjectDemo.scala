package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-9:56
  * @version 1.0.0
  */
object ClassObjectDemo {
  def main(args: Array[String]) {
    val myCounter = new Counter()
    myCounter.increment(5)
    println(myCounter.current)
  }
}
