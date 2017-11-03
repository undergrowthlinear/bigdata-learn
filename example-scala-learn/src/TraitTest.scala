/**
  * @description
  * @author zhangwu
  * @date 2017-11-03-23:54
  * @version 1.0.0
  */
trait Equal {
  def isEqual(x: Any): Boolean
  def isNotEqual(x: Any): Boolean = !isEqual(x)
}

class Point1(xc: Int, yc: Int) extends Equal {
  var x: Int = xc
  var y: Int = yc
  def isEqual(obj: Any) : Boolean=
    {
      obj.isInstanceOf[Point1] &&
        obj.asInstanceOf[Point1].x == x
    }
}

object TraitTest {
  def main(args: Array[String]) {
    val p1 = new Point1(2, 3)
    val p2 = new Point1(2, 4)
    val p3 = new Point1(3, 3)

    println(p1.isNotEqual(p2))
    println(p1.isNotEqual(p3))
    println(p1.isNotEqual(2))
  }
}
