package scalainprogramming.moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-09-21:51
  * @version 1.0.0
  */
class ApplicationDemo {
  private var sum = 0

  def add(num: Integer): Unit = sum += num
}

object ApplicationDemo {

  def main(args: Array[String]) {
    val aD = new ApplicationDemo
    println(aD.sum)
    println(aD.add(10))
    println(aD.sum)
  }

}
