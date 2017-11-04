package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-04-13:47
  * @version 1.0.0
  */
object ClassTest {
  def main(args: Array[String]) {
    val pt = new Point(10, 20);

    // 移到一个新的位置
    pt.move(10, 10);
    val loc = new Location(10, 20, 15);

    // 移到一个新的位置
    loc.move(10, 10, 5);
  }
}
