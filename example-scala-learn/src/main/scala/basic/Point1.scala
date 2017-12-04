package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-13:47
  * @version 1.0.0
  */
class Point1(xc: Int, yc: Int) extends Equal {
  var x: Int = xc
  var y: Int = yc

  def isEqual(obj: Any): Boolean = {
    obj.isInstanceOf[Point1] &&
      obj.asInstanceOf[Point1].x == x
  }
}
