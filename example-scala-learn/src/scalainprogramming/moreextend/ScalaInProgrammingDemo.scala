package scalainprogramming.moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-23:40
  * @version 1.0.0
  */
object ScalaInProgrammingDemo {

  def map_demo(): Unit = {
    var capital = Map("US" -> "Washington", "France" -> "Paris")

    capital += ("Japan" -> "Tokyo")

    println(capital("France"))
  }

  def main(args: Array[String]) {
    map_demo()
  }

}
