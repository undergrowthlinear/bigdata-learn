package basic

import scala.collection.mutable


/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-11-06-11:40
  * @version 1.0.0
  */

object LeakageInspectionCompletion {

  def demo_datatype_array(): Unit = {
    val a1 = Array(1, 2, 3)
    println(a1.toString)
    println(a1.length)
    println(a1.mkString)
    val a = Array(1, 2)
    val b = Array.ofDim[Int](2)
    val c = Array.concat(a, b)
    println(c.mkString(","))
  }

  def demo_datatype_seq(): Unit = {
    val a1 = Seq(1, 2, 3)
    println(a1.mkString(","))
    var seq = Seq[String]()
    seq = seq :+ "hello"

    var set = Set[String]()
    set += "hello"
    println(seq)
    println(set)
    val l = List(1, 2)
    println(l)
  }

  def toNum(q: scala.collection.mutable.Queue[Int]) = {
    var num = 0
    while (!q.isEmpty) {
      num *= 10
      num += q.dequeue
    }
    num
  }

  def demo_instance(): Unit = {
    println("1".isInstanceOf[String])
    // 类型擦除
    println(List(1).isInstanceOf[List[String]])
  }


  def add(x: Int)(y: Int) = x + y

  def demo_curry() = {
    val list = List(1, 2, 3)
    println(list.foldLeft("res:")((x: String, y: Int) => x + y))
    // def add(x:Int)=(y:Int)=>x+y
    val result = add(1) _
    val sum = result(2)
    println(result)
    println(sum)
    // add(1)(2) 实际上是依次调用两个普通函数（非柯里化函数），第一次调用使用一个参数 x，返回一个函数类型的值，第二次使用参数y调用这个函数类型的值
    println(add(1)(2))
  }

  def sum(f: Int => Int, a: Int, b: Int): Int =
    if (a > b) 0 else f(a) + sum(f, a + 1, b)

  def id(x: Int): Int = x

  def square(x: Int): Int = x * x

  def powerOfTwo(x: Int): Int = if (x == 0) 1 else 2 * powerOfTwo(x - 1)

  def sumInts(a: Int, b: Int): Int = sum(id, a, b)

  def sumSquared(a: Int, b: Int): Int = sum(square, a, b)

  def sumPowersOfTwo(a: Int, b: Int): Int = sum(powerOfTwo, a, b)


  def demo_high_function_curry() = {
    // 高阶函数----带其他函数做参数的函数
    println(sumInts(1, 10))
    println(sumSquared(1, 10))
    println(sumPowersOfTwo(1, 10))
  }

  def demo_require() = {
    require(true, "right")
  }

  def demo_vector() = {
    val v=Vector(1,2,3)

  }

  def main(args: Array[String]): Unit = {
    println("LeakageInspectionCompletion")
    println("================ ================")
    // 数据类型演示
    demo_datatype_array()
    println("================ ================")
    // seq
    demo_datatype_seq()
    println("================ ================")
    // val
    println(toNum(mutable.Queue(1, 2)))
    println("================ ================")
    // asInstanceOf
    demo_instance()
    println("================ ================")
    demo_curry()
    println("================ ================")
    demo_high_function_curry()
    println("================ ================")
    demo_require()
    println("================ ================")
    demo_vector()
    println("================ ================")
    println("================ ================")
  }

}
