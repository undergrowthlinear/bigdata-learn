package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-03-23:48
  * @version 1.0.0
  */
/* 文件名：Marker.scala
 * author:菜鸟教程
 * url:www.runoob.com
 */

// 私有构造方法
class Marker private(val color: String) {

  println("创建" + this)

  override def toString(): String = "颜色标记：" + color

}

// 伴生对象，与类共享名字，可以访问类的私有属性和方法
object Marker {

  private val markers: Map[String, Marker] = Map(
    "red" -> new Marker("red"),
    "blue" -> new Marker("blue"),
    "green" -> new Marker("green")
  )

  def main(args: Array[String]) {
    println(Marker("red"))
    // 单例函数调用，省略了.(点)符号
    println(Marker getMarker "blue")
  }

  def apply(color: String) = {
    if (markers.contains(color)) markers(color) else null
  }

  def getMarker(color: String) = {
    if (markers.contains(color)) markers(color) else null
  }
}