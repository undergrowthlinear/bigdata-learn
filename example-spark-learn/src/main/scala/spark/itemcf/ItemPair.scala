package spark.itemcf

;

/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-11-15-16:18
  * @version 1.0.0
  */
class ItemPair(item1: Int, item2: Int) extends Serializable {
  var x: Int = math.min(item1, item2)
  var y: Int = math.max(item1, item2)

  override def toString: String = super.toString

  def canEqual(other: Any): Boolean = other.isInstanceOf[ItemPair]

  override def equals(other: Any): Boolean = other match {
    case that: ItemPair =>
      (that canEqual this) &&
        x == that.x &&
        y == that.y
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(x, y)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
