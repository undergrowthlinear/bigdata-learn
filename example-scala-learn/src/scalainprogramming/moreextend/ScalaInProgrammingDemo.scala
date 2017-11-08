package scalainprogramming.moreextend

import scala.io.Source

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

  def list_demo() = {
    var li = List(1, 2, 3)
    li.foreach(println)
    println(li.isEmpty)
    if (!(li.isEmpty)) {
      println(li.mkString(","))
    }
  }

  def tuple_demo(): Unit = {
    // 存放不同类型/以.与_与1开始访问
    var pair = Tuple2(1, "hello world")
    println(pair._1 + "\t" + pair._2)
  }

  def set_demo(): Unit = {
    var set = Set(1, 2, 2)
    set.foreach(println)
  }

  def noother_demo(a1: Array[String]) = a1.mkString("\n")


  def read_demo(args: Array[String]): Unit = {
    if (args.length < 0) println("请输入文件名")
    val list = Source.fromFile(args(0)).getLines().toList
    val longLine = list.reduceLeft((a, b) => if (a.length > b.length) a else b)
    val maxWidth = longLine.length
    list.foreach(line => {
      val paddNum = " " * (maxWidth - line.length)
      println(paddNum + "\t" + line.length + "\t" + line)
    })
  }

  def main(args: Array[String]) {
    map_demo()
    for_demo()
    array_demo()
    list_demo()
    tuple_demo()
    set_demo()
    val a = noother_demo(Array("hello", "world"))
    println(a)
    read_demo(args)
  }

}
