# 参考
- https://github.com/databricks/learning-spark
# 概念
- 基于内存----快速/通用----计算
- Spark 是一个用来实现快速而通用的集群计算的平台
# 生态
- 2009年伯克利分校----基于mapreduce----使用scala编写
# 组件
### 上层组件----spark sql/spark streaming/mlib/graphx
- spark sql----Spark 用来操作结构化数据的程序包
- spark streaming----Spark 提供的对实时数据进行流式计算的组件
- mlib----提供常见的机器学习（ML）功能的程序库
- graphx----用来操作图（比如社交网络的朋友关系图）的程序库
### 中间层----spark core
- 弹性分布式数据/rdd(RDD 表示分布在多个计算节点上可以并行操作的元素集合)
### 底层----集群管理器
- 独立调度器----Spark自带/默认
- Hadoop YARN
- Apache Mesos
# 应用处理
- hadoop----离线处理/时效性不高
- spark----时效性高/机器学习
# 命令
- start-master----启动master
- start-class----启动worker
- spark-submit----提交作业
# RDDs
- 弹性分布式数据集/不可变分布式的集合元素/计算和抽象的基础
- 驱动器程序通过一个SparkContext 对象来访问Spark。这个对象代表对计算集群的一个连
  接。
- rdd血统关系图----rdd之间的依赖与创建关系/延迟计算/第一次action的时候计算
- driver program
  - 包含main/SparkContext
- SparkContext
  - 代表和一个集群的连接
- 分片/分区----运行在集群中不同的节点
  - 属于rdd的一部分/是并行处理单元
- 创建----读取外部数据集/驱动器程序集合并行化
## 操作----转换操作(转换操作返回的是RDD)与行动操作(行动操作返回的是其他类型)
- trans----由一个rdd生成另一个新的rdd/
  - map----接收函数 应用到rdd每个元素 返回新的rdd
  - filter----接收函数返回新的rdd
  - flatMap----压扁 一行转多行
  - 集合运算----distinct/union/intersection/subtract
- action----对rdd计算结果----返回结果给驱动程序或者保存到外部系统----
  - reduce----相同类型进行操作
  - collect----
  - take----返回n个结果 随机的
  - top----排序后的top
  - foreach----计算元素 不返回本地
  - persist----持久化
  - count----
  - first----