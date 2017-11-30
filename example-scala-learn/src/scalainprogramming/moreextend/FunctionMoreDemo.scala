package scalainprogramming.moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-13-21:44
  * @version 1.0.0
  */
object FunctionMoreDemo {

  def main(args: Array[String]) {
    val i = 10
    // 类成员
    method_demo()
    // 内嵌方法
    def inner_meth(): Unit = {
      println(i)
    }
    inner_meth()
    // 函数字面值
    val t = (i: Int) => i + 20
    println(t(200))
    // 占位符替换
    val list = List(1, 2, 3)
    list.filter(_ > 2).foreach(println)
    // 重复参数 _*(当做每一个参数传递)
    repeat_demo("1", "2")
  }

  def method_demo(): Unit = {
    val what = new WhatFunction
    what.say("hello world")
  }

  def repeat_demo(ar: String*): Unit = {
    ar.foreach(println)
  }

}

class WhatFunction {
  def say(s: String): Unit = {
    println(s)
  }
}
