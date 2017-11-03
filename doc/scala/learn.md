# 概念
- Scala 源代码被编译成Java字节码，所以它可以运行于JVM之上，并可以调用现有的Java类库
```hello world
object HelloWorld {
    def main(args: Array[String]): Unit = {
        println("Hello, world!")
    }
}
```
- Scala是一种纯面向对象的语言，每个值都是对象。对象的数据类型以及行为由类和特质描述
- Scala也是一种函数式语言，其函数也能当成值来使用
- Scala使用Actor作为其并发模型,在2.10之后的版本中，使用Akka作为其默认Actor实现
# Scala 基础语法
- Scala 语句末尾的分号 ; 是可选的
- 区分大小写/类名/方法名称/程序文件名----与java类似 (驼峰)
- def main(args: Array[String]) - Scala程序从main()方法开始处理，Scala程序的强制程序入口部分
- Scala 关键字不同于java----def/forSome/implicit/match/lazy/sealed/trait/val/var/type
- Scala 类似 Java 支持单行和多行注释----// /* */
- Scala 包----package com.runoob/import java.awt._
- 默认情况下，Scala 总会引入 java.lang._ 、 scala._ 和 Predef._
- Byte/Short Char/Int Float/Long Double/String/Boolean/Unit/Null/Nothing/Any/AnyRef
- '<标识符>/'/"/"""/var/val  VariableName : DataType [=  Initial Value]
- private，protected，public/默认情况下，Scala对象的访问级别都是 public/
  - Scala 中的 private 限定符，比 Java 更严格，在嵌套类情况下，外层类甚至不能访问被嵌套类的私有成员
  - 保护（Protected）成员,因为它只允许保护成员在定义了该成员的的类的子类中被访问
  - 写成private[x],读作"这个成员除了对[…]中的类或[…]中的包中的类及它们的伴生对像可见外，对其它所有类都是private
- if else 与java一致/循环与java一致
- Scala 函数----Scala 方法是类的一部分，而函数是一个对象可以赋值给一个变量。换句话来说在类中定义的函数即是方法
  - def functionName ([参数列表]) : [return type]
  - 不写等于号和方法主体，那么方法会被隐式声明为"抽象(abstract)"，包含它的类型于是也是一个抽象类型
  - functionName( 参数列表 )
- 闭包是一个函数，返回值依赖于声明在函数外部的一个或多个变量
-  Scala 中，字符串的类型实际上是 Java String，它本身没有 String 类。/new StringBuilder
- var z = new Array[String](3)/var z = Array("Runoob", "Baidu", "Google")
- Scala Collection----可变的和不可变的集合
- Scala 类和对象----Scala中的类不声明为public，一个Scala源文件中可以有多个类/Scala 的类定义可以有参数
  - 重写一个非抽象方法必须使用override修饰符、只有主构造函数才可以往基类的构造函数里写参数
  - 在子类中重写超类的抽象方法时，你不需要使用override关键字/Scala 只允许继承一个父类
  - Scala 中，是没有 static 这个东西的，object对象不能带参数
- Scala Trait(特征)----与接口不同的是，它还可以定义属性和方法的实现
  - 它使用的关键字是 trait
  - Scala的类只能够继承单一父类，但是如果是 Trait(特征) 的话就可以继承多个，从结果来看就是实现了多重继承