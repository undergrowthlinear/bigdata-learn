package scalainprogramming.moreextend

import java.io.{File, PrintWriter}
import java.util.Date


/**
  * @description
  * @author zhangwu
  * @date 2017-11-14-20:49
  * @version 1.0.0
  */
object FileMatcher {

  def fileEnding2(query: String) = filesMatching(query, (name: String, query: String) => name.endsWith(query))

  def fileContains(query: String) = filesMatching(query, _.contains(_))

  def fileRegex(query: String) = filesMatching(query, _.matches(_))

  // 以函数作为参数传递
  def filesMatching(query: String, matcher: (String, String) => Boolean) = {
    for (file <- fileHere; if matcher(file.getName, query)) yield file
  }

  private def fileHere = (new java.io.File(".")).listFiles()

  def main(args: Array[String]) {
    println(containNge(List(1, 2, 3)))
    println(containNge(List(1, -2, 3)))
    fileEnding("scala").foreach(println)
    // 柯里化函数 调用
    println(add(1) _)
    println(add2(1, 2))
    println(twice(_ + 1, 5))
    // 借贷模式
    withPrintWriter(new File("load.txt"), writer => writer.println(new Date))
    // 使用花括号替换小括号
    withPrintWriter2(new File("load1.txt")) {
      writer => writer.println(new Date)
    }
    //
    val a: Any = 12
  }

  // 简化客户端代码
  def containNge(num: List[Int]) = num.exists(_ < 0)

  // _ 占位符
  def fileEnding(query: String) = filesMatching(query, _.endsWith(_))

  // 柯里化函数
  def add(x: Int)(y: Int) = x + y

  def add2(x: Int, y: Int) = x + y

  // 新的控制语句
  def twice(op: Double => Double, x: Double) = op(op(x))

  // 借贷模式
  def withPrintWriter(file: File, op: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

  // 柯里化 借贷模式 使用花括号替换小括号
  def withPrintWriter2(file: File)(op: PrintWriter => Unit) = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

}
