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