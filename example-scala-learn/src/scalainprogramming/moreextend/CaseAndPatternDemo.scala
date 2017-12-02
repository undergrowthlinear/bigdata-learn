
/**
  * @description
  * @author zhangwu
  * @date 2017-11-18-14:27
  * @version 1.0.0
  */
object CaseAndPatternDemo {

  // match是scala表达式 以结果结尾
  // scala备选项不会掉到下一个case
  // 备选项没有匹配 则会抛出异常
  def simpleOpe(expr: Expr): Expr = expr match {
    // 构造器模式
    case Unop("-", Unop("-", Var("x"))) => Var("x")
    case BiOp("+", Number(0.0d), Number(0.0d)) => Number(0.0d)
    case BiOp("*", Number(1.0d), Number(1d)) => Number(1d)
    // _ 为通配模式
    case _ => expr
  }

  def matchAng(i: Any): Any = i match {
    // 无用 因为scala采用与java一致的泛型擦除
    case m: Map[Int, Int] => true
    // 类型匹配 带模式守卫 超级强大 很多地方运用
    case x: String if x.length > 10 => x.length
    // 常量模式
    case true => true
    // 可进行元组匹配
    case (i) => println("x")
    // 序列匹配
    case List(0, _*) => println("list found")
    // case _ => _
  }


  def main(args: Array[String]) {
    simpleOpe(Unop("-", Unop("-", Var("x"))))
    val m = Map("x" -> "y")
    // Optional----包含Some(y)/None
    println(m.get("x").get)
    println(m.get("x"))
    println(m.get("z"))
    // 类型匹配
    println(matchAng("qwertyuiopq"))
  }

}

// 空类结构体可省略花括号{}
// sealed 封闭的样式
sealed abstract class Expr

// case 加上 class 表示样例类
// 为样例类添加类名一致的工厂方法
// 样本类的所有参数列表都是val变量
// 样本类使用重写的equals做比较
case class Var(name: String) extends Expr

case class Number(num: Double) extends Expr

case class Unop(operator: String, args: Expr) extends Expr

case class BiOp(operator: String, left: Expr, right: Expr) extends Expr
