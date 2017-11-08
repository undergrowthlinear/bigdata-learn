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

  def for_demo(): Unit = {
    var str = "hello world"
    for (s <- str.split(" ")) println(s)
    //
    str.split(" ").foreach(println)
    str.split(" ").foreach(s => println(s))
  }

  def array_demo(): Unit = {
    val a1: Array[String] = new Array[String](3);
    a1(0) = "hello world"
    // 数组赋值 默认调用update方法
    a1.update(1, "what world")
    // 简洁的创建数组方式  类型scala可自动推断
    val a2 = Array("hello world", "what world")
    // 方法是一等公民
    a1.foreach(println)
    a2.foreach(println)
  }

  def main(args: Array[String]) {
    map_demo()
    for_demo()
    array_demo()
  }

}
