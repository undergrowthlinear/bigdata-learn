package basic

import scala.util.matching.Regex

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-13:47
  * @version 1.0.0
  */
object RegexTest {

  def regex_demo(): Unit = {
    val pattern = new Regex("(S|s)cala")  // 首字母可以是大写 S 或小写 s
    val str = "Scala is scalable and cool"

    println((pattern findAllIn str).mkString(","))   // 使用逗号 , 连接返回结果
  }

  def regex_demo2(): Unit = {
    val pattern = "(S|s)cala".r
    val str = "Scala is scalable and cool"

    println(pattern replaceFirstIn(str, "Java"))
  }

  def regex_demo3(): Unit = {
    val pattern = new Regex("abl[ae]\\d+")
    val str = "ablaw is able1 and cool"

    println((pattern findAllIn str).mkString(","))
  }

  def main(args: Array[String]) {
    val pattern = "Scala".r
    val str = "Scala is Scalable and cool"

    println(pattern findFirstIn str)

    regex_demo()
    regex_demo2()
    regex_demo3()
  }


}
