# 参考
- http://www.scala-sbt.org/0.13/docs/zh-cn/Running.html
## 命令
- clean----删除所有生成的文件 （在 target 目录下）
- compile----编译源文件（在 src/main/scala 和 src/main/java 目录下）。
- test----编译和运行所有测试。
- console----进入到一个包含所有编译的文件和所有依赖的 classpath 的 Scala 解析器。输入 :quit， Ctrl+D （Unix），或者 Ctrl+Z （Windows） 返回到 sbt。
- run ----在和 sbt 所处的同一个虚拟机上执行项目的 main class。
- package----将 src/main/resources 下的文件和 src/main/scala 以及 src/main/java 中编译出来的 class 文件打包成一个 jar 文件。
- help ----显示指定的命令的详细帮助信息。如果没有指定命令，会显示所有命令的简介。
- reload----重新加载构建定义（build.sbt， project/*.scala， project/*.sbt 这些文件中定义的内容)。在修改了构建定义文件之后需要重新加载。
## 概念
- 一个构建定义是一个Project，拥有一个类型为 Setting[T] 的列表，Setting[T] 是会影响到 sbt 保存键值对的 map 的一种转换，T 是每一个 value 的类型。