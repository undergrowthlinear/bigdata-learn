package moreextend

/**
  * @description
  * @author zhangwu
  * @date 2017-11-11-21:10
  * @version 1.0.0
  */
object FunctionObject {

  def main(args: Array[String]) {
    val r1 = new Rational(10, 20)
    val r2 = new Rational(1, 2)
    val r3 = new Rational(1, 3)
    val r4 = new Rational(4)
    println(r1 + r2)
    println(r2 * r3)
    println(r2.lessThat(r3))
    println(r2.max(r3))
    println(r4)
    println(r2 - r2)
    println(r2 + 5)
    println(r2 / r3)
    println(r2 + r3)
    println(r2 - r3)
    println(r2 * r3)
    println(r2 / r3)
    // 隐式转换 定义在class里面不起作用
    // println(2 * r2)
    //println(new Rational(10, 0))
  }

}

// n d 表示为类参数
// 没有类主体就不需要一对空的花括号
// scala编译器会收集n d构建主构造器
// scala编译器把类中既不是字段也不是方法的定义会编入主构造器中
class Rational(n: Int, d: Int) {
  // println("Create Me ")
  // 构造参数的先决条件
  require(d != 0)

  private val g = gcd(n.abs, d.abs)

  var num = n / g
  var denom = d / g

  // 定义操作符方法
  def +(that: Rational): Rational = {
    new Rational(num * that.denom + that.num * denom, denom * that.denom)
  }  // 只有主类构造器能够调用超类构造器
  // 辅助构造器 都是以this开头
  def this(n: Int) = this(n, 1)

  // 隐式转换 需要定义在作用范围内才起作用
  implicit def intToRational(x: Int) = new Rational(x)

  // 方法重载
  def +(i: Int): Rational = {
    new Rational(num + i * denom, denom)
  }

  // 定义操作符方法
  def -(that: Rational): Rational = {
    new Rational(num * that.denom - that.num * denom, denom * that.denom)
  }

  // 方法重载
  def -(i: Int): Rational = {
    new Rational(num - i * denom, denom)
  }

  def *(that: Rational): Rational = {
    new Rational(num * that.num, denom * that.denom)
  }

  def *(i: Int): Rational = {
    new Rational(num * i, denom)
  }

  def /(that: Rational): Rational = {
    new Rational(num * that.denom, denom * that.num)
  }

  def /(i: Int): Rational = {
    new Rational(num, denom * i)
  }

  def max(that: Rational): Rational = {
    if (this.lessThat(that)) that else this
  }

  // this 自指向
  def lessThat(that: Rational): Boolean = {
    this.num * that.denom < that.num * this.denom
  }

  // 重载 toString
  override def toString = num + "/" + denom

  private def gcd(a: Int, b: Int): Int = {
    if (b == 0) a else gcd(b, a % b)
  }



}
