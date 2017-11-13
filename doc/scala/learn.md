# 参考
- http://www.runoob.com/scala/scala-file-io.html
- http://www.jianshu.com/p/e0fc0ab7a9d2
- http://blog.csdn.net/fjse51/article/details/52152362
# 概念
## 名词
- 可扩展语言/是面向对象与函数式编程加入静态类型的混合体
- scala像集市而非大教堂,兼容性/简洁/高级抽象/高级静态类型化
- Scala 源代码被编译成Java字节码，所以它可以运行于JVM之上，并可以调用现有的Java类库
```hello world
object HelloWorld {
    def main(args: Array[String]): Unit = {
        println("Hello, world!")
    }
}
```
- Scala是一种纯面向对象的语言，每个值都是对象,每个操作都是方法的调用,对象的数据类型以及行为由类和特质描述
- Scala也是一种函数式语言，其函数也能当成值来使用,当做参数/返回值/定义匿名函数,方法不应该有副作用
- Scala使用Actor作为其并发模型,在2.10之后的版本中，使用Akka作为其默认Actor实现
# Scala 基础语法
- Scala 语句末尾的分号 ; 是可选的
- 区分大小写/类名/方法名称/程序文件名----与java类似 (驼峰)
- def main(args: Array[String]) - Scala程序从main()方法开始处理，Scala程序的强制程序入口部分
- Scala 关键字不同于java----def/forSome/implicit/match/lazy/sealed/trait/val/var/type
- Scala 类似 Java 支持单行和多行注释----// /* */
- Scala 包----package com.runoob/import java.awt._
- 默认情况下，Scala 总会引入 java.lang._ 、 scala._ 和 Predef._
- Boolean/Byte Char/Short Int/Float/ Long/Double/ String/Unit/Null/Nothing/Any/AnyRef
- '<标识符>/'/"/"""/var/val/类型推断
  - VariableName : DataType [=  Initial Value]----与java相反
  - +----字符串连接
- private，protected，public/默认情况下，Scala对象的访问级别都是 public/
  - Scala 中的 private 限定符，比 Java 更严格，在嵌套类情况下，外层类甚至不能访问被嵌套类的私有成员
  - 保护（Protected）成员,因为它只允许保护成员在定义了该成员的的类的子类中被访问
  - 写成private[x],读作"这个成员除了对[…]中的类或[…]中的包中的类及它们的伴生对像可见外，对其它所有类都是private
- if else 与java一致/while 循环与java一致/for(val 常量 <-(其中) 变量组)----发生器(to/until)----yield产生新的集合
  - if/while/for/try/match/函数调用----能够产生某个值
  - try/catch/finally----catch/case
  - match/case---- _表示默认情况----更简洁的使用
- Scala 函数----Scala 方法是类的一部分，而函数是一个对象可以赋值给一个变量。换句话来说在类中定义的函数即是方法
  - def functionName ([参数列表]) : [return type]----比java多了def,类型在名称之后
  - :[return type]可省略,类型推断器会推断,{}如果是一行语句,也可省略
  - 不写等于号和方法主体，那么方法会被隐式声明为"抽象(abstract)"，包含它的类型于是也是一个抽象类型
  - functionName( 参数列表 )
  - 函数式编程风格----函数是一等民----函数字面量(一行时,可省略)----(右箭头/=>)----函数体
    - 类中方法/本地嵌套方法/函数字面量(使用=>)/闭包捕获了变量本身/重复参数(*)/尾递归
  - 指令式编程风格----
    - 指令转函数----尽量不使用var编程/尽量函数完全无副作用(除了返回值,对主调函数无任何影响)
- 闭包是一个函数，返回值依赖于声明在函数外部的一个或多个变量
-  Scala 中，字符串的类型实际上是 Java String，它本身没有 String 类。/new StringBuilder
- var z = new Array[String](3)/var z = Array("Runoob", "Baidu", "Google")
  - 数组以0开始,z(0)而不是java的z[0]
  - Array[String]----属于类型参数，3----值参数
  - 任何对于对象的值参数应用将都被转换为对apply方法的调用/对带有括号并包含参数赋值时,转换为对对象的update方法调用
- Scala Collection----可变的和不可变的集合
  - List(Nil)/同类型/不可变----::(方法被右操作数调用)/:::/foreach/length/mkString/isEmpty
  - Tuple/不同类型/不可变----元祖实例化对象放入()/以.与_与数字为1开始的索引访问/par._1
  - map/set----默认为不可变/也有可编辑支持,引入不同包即可
- 任何对象都能调用的->机制被称为隐式转化(implicit conversion)
    - Boolean/Byte Char/Short Int/Float/ Long/Double/ String/Unit/Null/Nothing/Any/AnyRef
    - 前缀操作符(+/-/!/~)/中缀操作符/后缀操作符
    - scala操作符就是方法调用,scala基本类型的富变体的隐式转换可以增加更多有用的方法
- Scala 类和对象()----Scala中的类不声明即为public，一个Scala源文件中可以有多个类/Scala 的类定义可以有参数
  - class/new/var/val(方法参数为val)/def(带花括号没有等号相当于Unit结果类型的方法)/private(只能在类内部)
  - singleton object----与java静态类类似/虚构类的名称是对象名加上一个美元符号
  - companion object----类和伴生对象可相互访问其私有成员
  - standalone object----
  - 函数式对象----任何不具有可改变状态的对象的类
  - 重写一个非抽象方法必须使用override修饰符、只有主构造函数才可以往基类的构造函数里写参数
  - 在子类中重写超类的抽象方法时，你不需要使用override关键字/Scala 只允许继承一个父类
  - Scala 中，是没有 static 这个东西的，object对象不能带参数
  - scala 中没有 static 关键字对于一个class来说，所有的方法和成员变量在实例被 new 出来之前都是无法访问的,
    - 因此class文件中的main方法也就没什么用了，scala object 中所有成员变量和方法默认都是 static 的所以 可以直接访问main方法
  - 在scala中，类名可以和对象名为同一个名字，该对象称为该类的伴生对象，
    - 类和伴生对象可以相互访问他们的私有属性，但是他们必须在同一个源文件内。
- Scala Trait(特征)----与接口不同的是，它还可以定义属性和方法的实现
  - 它使用的关键字是 trait
  - Scala的类只能够继承单一父类，但是如果是 Trait(特征) 的话就可以继承多个，从结果来看就是实现了多重继承
  - Scala中也是一般只能继承一个父类，可以通过多个with进行多重继承
- Scala 模式匹配----选择器 match {备选项}
  - 一个模式匹配包含了一系列备选项，每个都开始于关键字 case。每个备选项都包含了一个模式及一到多个表达式。箭头符号 => 隔开了模式和表达式
  - case关键字的类定义就是就是样例类(case classes)，样例类是种特殊的类，经过优化以用于模式匹配
- Scala 正则表达式----http://www.runoob.com/scala/scala-regular-expressions.html
- Scala 异常处理----throw new IllegalArgumentException/与java类似
  - catch字句是按次序捕捉的。因此，在catch字句中，越具体的异常越要靠前，越普遍的异常越靠后
- Scala 提取器(Extractor)
  - Scala 提取器是一个带有unapply方法的对象。unapply方法算是apply方法的反向操作：
  - unapply接受一个对象，然后从对象中提取值，提取的值通常是用来构造该对象的值
  - 在我们实例化一个类的时，可以带上0个或者多个的参数，
  - 编译器在实例化的时会调用 apply 方法。我们可以在类和对象中都定义 apply 方法
- Scala 文件 I/O----Scala 进行文件写操作，直接用的都是 java中 的 I/O 类 （java.io.File)：