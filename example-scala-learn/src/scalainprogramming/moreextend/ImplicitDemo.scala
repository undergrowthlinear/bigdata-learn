import scalainprogramming.moreextend.Rational

/**
  * @description
  * @author zhangwu
  * @date 2017-11-19-22:47
  * @version 1.0.0
  */
object ImplicitDemo {

  // 隐式转换 参看 代码 String2Seq.scala
  // implicit 用于变量 方法 对象定义 ,用于标记可以让编译器进行隐式转换的定义
  // 隐式转换必须以单一标识符的形式处于作用域 或与转换的源、目标类型关联在一起
  // 无歧义规则----隐式转换唯有在不存在其他可插入转换的前提下才能插入
  // 单一操作规则----只会尝试一个隐式操作
  // 隐式操作地方----转换为
  // 期望类型 ----
  implicit def doubleToInt(d: Double) = {
    println("double to int")
    d.toInt
  }

  // 指定调用者的转换(接收者的转换) ----
  implicit def intToRational(i: String) = new Rational(10, 10)

  // 隐式参数 ----
  implicit def intToString(i: Int) = i.toString

  /*implicit def stringWrapper(s: String) =
    new RandomAccessSeq[Char] {
      def length = s.lengthe
      def apply(i: Int) = s.charAt(i)
    }*/


  def main(args: Array[String]) {
    val i: Int = 10.5d
    println("1" + new Rational(1, 1))
    // 隐式参数 类似于扩展柯里化函数
    // 隐式参数的类型名称至少要用一个角色去确定
    implicit val p: ProPre = new ProPre("what")
    implicit val p1: ProDrink = new ProDrink("hello world")
    Greeter.greet("what")
  }
}

class ProPre(val propre: String)

class ProDrink(val propre: String)

object Greeter {
  def greet(name: String)(implicit prompt: ProPre, drink: ProDrink): Unit = {
    println(name)
    println(prompt.propre)
    println(drink.propre)
  }
}

