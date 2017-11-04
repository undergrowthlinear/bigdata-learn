package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-03-22:59
  * @version 1.0.0
  */
import scala.Array._
object add{

  def array_concat_test(): Unit = {
    var myList1 = Array(1.9, 2.9, 3.4, 3.5)
    var myList2 = Array(8.9, 7.9, 0.4, 1.5)

    var myList3 =  concat( myList1, myList2)

    // 输出所有数组元素
    for ( x <- myList3 ) {
      println( x )
    }
  }

  def array_range_test(): Unit = {
    var myList1 = range(10, 20, 2)
    var myList2 = range(10,20)

    // 输出所有数组元素
    for ( x <- myList1 ) {
      print( " " + x )
    }
    println()
    for ( x <- myList2 ) {
      print( " " + x )
    }
  }

  def collection_demo(): Unit = {
    // 定义整型 List
    val x = List(1,2,3,4)

    // 定义 Set
    var x1 = Set(1,3,5,7)

    // 定义 Map
    val x2 = Map("one" -> 1, "two" -> 2, "three" -> 3)

    // 创建两个不同类型元素的元组
    val x3 = (10, "Runoob")

    // 定义 Option
    val x4:Option[Int] = Some(5)

    val it = Iterator("Baidu", "Google", "Runoob", "Taobao")

    while (it.hasNext){
      println(it.next())
    }
    var ix=x.iterator;
    while (ix.hasNext){
      println(ix.next())
    }

    val ita = Iterator(20,40,2,50,69, 90)
    val itb = Iterator(20,40,2,50,69, 90)

    println("最大元素是：" + ita.max )
    println("最小元素是：" + itb.min )
    println("ita.size 的值: " + ita.size )
    println("itb.length 的值: " + itb.length )
  }

  def matchTest(x: Any): Any = x match {
    case 1 => "one"
    case "two" => 2
    case y: Int => "scala.Int"
    case _ => "many"
  }

  def main(args: Array[String]) {
    /*println( "Returned Value : " + addInt(5,7) );
    println( "muliplier(1) value = " +  multiplier(1) )
    string_test()
    array_test()
    array_demo_test()
    array_concat_test()
    array_range_test()
    collection_demo()*/
    println(matchTest("two"))
    println(matchTest("test"))
    println(matchTest(1))
    println(matchTest(6))
  }

  def addInt( a:Int, b:Int ) : Int = {
    var sum:Int = 0
    sum = a + b

    return sum
  }

  var factor = 3
  val multiplier = (i:Int) => i * factor

  def string_test(): Unit ={
    val buf = new StringBuilder;
    buf += 'a'
    buf ++= "bcdef"
    println( "buf is : " + buf.toString )
    var palindrome = "www.runoob.com";
    var len = palindrome.length();
    println( "String Length is : " + len );
    println("菜鸟教程官网： ".concat("www.runoob.com"))
    var floatVar = 12.456
    var intVar = 2000
    var stringVar = "菜鸟教程!"
    var fs = printf("浮点型变量为 " +
      "%f, 整型变量为 %d, 字符串为 " +
      " %s", floatVar, intVar, stringVar)
    println(fs)
  }

  def array_test():Unit={
    var myList = Array(1.9, 2.9, 3.4, 3.5)

    // 输出所有数组元素
    for ( x <- myList ) {
      println( x )
    }

    // 计算数组所有元素的总和
    var total = 0.0;
    for ( i <- 0 to (myList.length - 1)) {
      total += myList(i);
    }
    println("总和为 " + total);

    // 查找数组中的最大元素
    var max = myList(0);
    for ( i <- 1 to (myList.length - 1) ) {
      if (myList(i) > max) max = myList(i);
    }
    println("最大值为 " + max);
  }

  def array_demo_test():Unit={
    var myMatrix = ofDim[Int](3,3)

    // 创建矩阵
    for (i <- 0 to 2) {
      for ( j <- 0 to 2) {
        myMatrix(i)(j) = j;
      }
    }

    // 打印二维阵列
    for (i <- 0 to 2) {
      for ( j <- 0 to 2) {
        print(" " + myMatrix(i)(j));
      }
      println();
    }
  }

}
