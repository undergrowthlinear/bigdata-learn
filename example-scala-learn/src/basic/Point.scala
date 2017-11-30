package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-03-23:30
  * @version 1.0.0
  */
class Point(val xc: Int, val yc: Int) {
  var x: Int = xc
  var y: Int = yc

  def move(dx: Int, dy: Int) {
    x = x + dx
    y = y + dy
    println("x 的坐标点 : " + x);
    println("y 的坐标点 : " + y);
  }
}
