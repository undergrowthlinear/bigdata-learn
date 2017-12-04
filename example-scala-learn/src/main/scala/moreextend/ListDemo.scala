package moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-19-18:56
  * @version 1.0.0
  */
object ListDemo {

  // head tail 运行时间都是常量
  // init last 时间与列表长度成正比
  // reverse
  // /:----向左操作符  :\----向右操作符
  def display(l: List[Int]): Unit = {
    println(l take 1)
    println(l drop 1)
    println(l.splitAt(1))
    println(l.head)
    println(l.tail)
    println(l.isEmpty)
    l.map(_ + 1)
  }

  // 分而治之
  def append(x: List[Int], y: List[Int]): List[Int] = x match {
    case List() => y
    case x :: xs1 => x :: append(xs1, y)
  }

  // list的所有元素都具有相同类型
  // list是协变的 List[Nothing] 是List[T] 所有类型的子类
  // Nil(空列表)与::(前缀连接符) 构造列表
  // ListBuffer(+=/toList)----缓存的list
  def main(args: Array[String]) {
    val l = List(1, 2)
    // 右结合规则
    val li = 1 :: 2 :: Nil
    display(l)
    display(li)
    // ::: 连接----右结合
    display(l ::: li)
    // 利用分而治之
    display(append(l, li))
    display(List.range(1, 10, 2))
  }
}
