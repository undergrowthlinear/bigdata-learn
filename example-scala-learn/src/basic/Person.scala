package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-03-23:42
  * @version 1.0.0
  */
class Person {
  var name = ""
  override def toString = getClass.getName + "[name=" + name + "]"
}
