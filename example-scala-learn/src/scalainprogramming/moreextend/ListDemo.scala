package scalainprogramming.moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-19-18:56
  * @version 1.0.0
  */
object ListDemo {

  def display(l: List[Int]): Unit = {
    println(l.head)
    println(l.tail)
    println(l.isEmpty)
  }

  // list的所有元素都具有相同类型
  // list是协变的 List[Nothing] 是List[T] 所有类型的子类
  // Nil(空列表)与::(前缀连接符) 构造列表
  def main(args: Array[String]) {
    val l = List(1, 2)
    // 右结合规则
    val li = 1 :: 2 :: Nil
    display(l)
    display(li)
  }
}
