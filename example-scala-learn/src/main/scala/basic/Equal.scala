package basic

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
