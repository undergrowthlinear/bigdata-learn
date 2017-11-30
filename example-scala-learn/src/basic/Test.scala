package basic

/**
  * @description
  * @author zhangwu
  * @date 2017-11-03-22:34
  * @version 1.0.0
  */
object Test {
  def main(args: Array[String]) {
    println("Hello\tWorld\n\n");
    var myVar: Int = 10
    val myVal: String = "heloo world"
    val pa = (40, "Foo")
    println(myVar)
    println(myVal)
    println(pa)
    var a = 10;
    var b = 20;
    var c = 25;
    var d = 25;
    // 算术
    println("a + b = " + (a + b));
    println("a - b = " + (a - b));
    println("a * b = " + (a * b));
    println("b / a = " + (b / a));
    println("b % a = " + (b % a));
    println("c % a = " + (c % a));
    // 关系运算符
    println("a == b = " + (a == b));
    println("a != b = " + (a != b));
    println("a > b = " + (a > b));
    println("a < b = " + (a < b));
    println("b >= a = " + (b >= a));
    println("b <= a = " + (b <= a));
    // 逻辑运算符
    var a1 = true;
    var b1 = false;
    println("a && b = " + (a1 && b1));
    println("a || b = " + (a1 || b1));
    println("!(a && b) = " + !(a1 && b1));
    // 位运算符
    a = 60; /* 60 = 0011 1100 */
    b = 13; /* 13 = 0000 1101 */
    c = 0;
    c = a & b; /* 12 = 0000 1100 */
    println("a & b = " + c);

    c = a | b; /* 61 = 0011 1101 */
    println("a | b = " + c);

    c = a ^ b; /* 49 = 0011 0001 */
    println("a ^ b = " + c);

    c = ~a; /* -61 = 1100 0011 */
    println("~a = " + c);

    c = a << 2; /* 240 = 1111 0000 */
    println("a << 2 = " + c);

    c = a >> 2; /* 15 = 1111 */
    println("a >> 2  = " + c);

    c = a >>> 2; /* 15 = 0000 1111 */
    println("a >>> 2 = " + c);
    // 赋值运算符
    c = a + b;
    println("c = a + b  = " + c);

    c += a;
    println("c += a  = " + c);

    c -= a;
    println("c -= a = " + c);

    c *= a;
    println("c *= a = " + c);

    a = 10;
    c = 15;
    c /= a;
    println("c /= a  = " + c);

    a = 10;
    c = 15;
    c %= a;
    println("c %= a  = " + c);

    c <<= 2;
    println("c <<= 2  = " + c);

    c >>= 2;
    println("c >>= 2  = " + c);

    c >>= 2;
    println("c >>= a  = " + c);

    c &= a;
    println("c &= 2  = " + c);

    c ^= a;
    println("c ^= a  = " + c);

    c |= a;
    println("c |= a  = " + c);
  }
}
