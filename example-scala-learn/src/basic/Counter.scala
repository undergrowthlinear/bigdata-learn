package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-13:47
  * @version 1.0.0
  */
class Counter {
  private var value = 0

  def increment(step: Int): Unit = {
    value += step
  }

  def current(): Int = {
    value
  }
}
