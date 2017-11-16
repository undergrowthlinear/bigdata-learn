# 参考
- https://github.com/databricks/learning-spark
# 概念
- 基于内存----快速/通用----计算
- Spark 是一个用来实现快速而通用的集群计算的平台
# 生态
- 2009年伯克利分校----基于mapreduce----使用scala编写
# 组件
### 上层组件----spark sql/spark streaming/mlib/graphx
- spark sql----Spark 用来操作结构化/半结构化数据的程序包
  - 从各种数据源读取数据/支持各种方式的sql查询
    - hive查询
    - 支持udf(用户自定义函数)
  - SchemaRDD(DataFrame)----存放Row 对象的RDD，每个Row 对象代表一行记录
 ```
 SELECT SUM(user.favouritesCount), SUM(retweetCount), user.id FROM tweets
 GROUP BY user.id
 ```
- spark streaming----Spark 提供的对实时数据进行流式计算的组件/微批次架构
  - Spark Streaming 使用离散化流（discretized stream）作为抽象表示，叫作DStream
  - DStream 是随时间推移而收到的数据的序列
    - 一种是转化操作（transformation），会生成一个新的DStream
      - 无状态（stateless）----每个批次的处理不依赖于之前批次的数据
      - 有状态（stateful)----需要使用之前批次的数据或者是中间结果来计算当前批次的数据
    - 另一种是输出操作（output operation），可以把数据写入外部系统
- mlib----提供常见的机器学习（ML）功能的程序库
  - 待学习
- graphx----用来操作图（比如社交网络的朋友关系图）的程序库
### 中间层----spark core
- 弹性分布式数据/rdd(RDD 表示分布在多个计算节点上可以并行操作的元素集合)
### 底层----集群管理器
- local
- 独立调度器----Spark自带/默认
- Hadoop YARN
- Apache Mesos
# 应用处理
- hadoop----离线处理/时效性不高
- spark----时效性高/机器学习
## spark 集群运行
- spark驱动器节点----执行main方法的进程/创建SparkContext、创建RDD、转换RDD、行动操作RDD/驱动器节点进行任务的调度、数据的跟踪
  - 把用户程序转为任务----stage----task
  - 为执行器节点调度任务
- 集群管理器----启动执行器节点,有时启动驱动器节点()----主节点/工作节点
  - 独立调度器----Spark自带/默认
  - Hadoop YARN
  - Apache Mesos
- spark执行器节点
  - 运行驱动器任务,返回结果给驱动器
  - 提供RDD的内存式存储
# 命令
- start-master----启动master
- start-class----启动worker
- spark-submit----提交作业/部署应用
# RDDs
- 弹性分布式数据集/不可变分布式的集合元素/计算和抽象的基础
- 驱动器程序通过一个SparkContext 对象来访问Spark。这个对象代表对计算集群的一个连
  接。
- rdd血统关系图----rdd之间的依赖与创建关系/延迟计算/第一次action的时候计算
  - RDD有向无环图
    - 用户代码定义RDD的有向无环图
    - 行动操作把有向无环图强制转译为执行计划
      - 这个作业会包含一个或多个步骤，每个步骤其实也就是一波并行执行的计算任务
    - 任务于集群中调度并执行
  - Spark 会使用60％的空间来存储RDD，20% 存储数据混洗操作产生的数
    据，剩下的20% 留给用户程序
- driver program
  - 包含main/SparkContext
- SparkContext
  - 代表和一个集群的连接
- 分片/分区----运行在集群中不同的节点
  - 属于rdd的一部分/是并行处理单元
- 创建----读取外部数据集/驱动器程序集合并行化
- 键值对RDD(pair RDD)----用来进行聚合运算/元素为元组
  - reduceByKey----通过key 进行操作----返回新的rdd
  - groupByKey----进行分组
  - mapValues/flatMapValues----应用值
  - keys/values/sortByKey----返回key value sort
  - join----对两个rdd进行内连接
  - subtractByKey/rightOuterJoin/leftOuterJoin/cogroup
## 操作----转换操作(转换操作返回的是RDD)与行动操作(行动操作返回的是其他类型)
- trans----由一个rdd生成另一个新的rdd/
  - map----接收函数,应用到rdd每个元素,将函数的返回结果放入rdd
  - filter----接收函数返回新的rdd,将满足函数的元素放入rdd
  - flatMap----压扁,一行转多行,将返回的迭代器拍扁
  - 集合运算(rdd具有相同数据类型)----
    - distinct(shuffle开销很大)/union(包含重复数据)/
    - intersection(shuffle去重)/subtract(shuffle)/cartesian(笛卡尔积/所有可能组合)
  - countByKey/lookup/collectAsMap
- action----对rdd计算结果----返回结果给驱动程序或者保存到外部系统----
  - reduce----接收函数,操作两个rdd元素,返回同类型新元素
  - fold----与reduce一致,但是需要初始值
  - aggregate(zeroValue)(seqOp, combOp)----返回不同的数据类型
  - collect----获取整个rdd数据
  - take----返回n个结果 随机的
  - takeOrdered----按照指定顺序返回
  - top----排序后的top
  - takeSample----从数据集获取采样
  - foreach----计算元素 不返回本地
  - persist/unpersist----持久化(可内存/磁盘)
  - count----返回元素个数
  - countByValue----各元素在rdd中出现次数
  - first----
## scala编程
- 传递局部可序列化变量或顶级对象中的函数始终是安全的/val query_ = this.query
- 共享变量
  - 累加器----对信息进行聚合
    - val blankLines = sc.accumulator(0)/转化操作中，累加器通常只用于调试目的
  - 广播变量----高效分发较大对象
## 存储方式
- 文件格式与文件系统
    - 文本文件/JSON/CSV/SequenceFile/对象文件/Hadoop输入输出格式/protocol buffer
    - 本地文件系统/amazon s3/hdfs
- SPARK SQL 的结构化数据源
    - hive
- 数据库与键值存储
    - jdbc/cassandra/hbase/elasticsearch