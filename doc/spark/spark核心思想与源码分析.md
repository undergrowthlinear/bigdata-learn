# spark设计理念与基本架构
## 优势
- 快速的处理能力----中间与结果集存储内存,避免磁盘IO
- 易于使用----支持scala/spark/等
- 支持查询/流式计算、可用性高(支持多个集群模式)、丰富的数据源支持
## 概念
- RDD----弹性分布式数据集
- Partition----数据分区,一个RDD可划分成多个分区
- NarrowDependency----窄依赖,子RDD依赖父RDD中固定的Partition
- ShuffleDependency----宽依赖,子RDD依赖父RDD中所有的Partition
- DAG----有向无环图,即RDD之间的依赖关系
- Task----具体执行任务
- Job----用户提交的作业,一个Job包含一个或者多个Task
- Stage----Job分成的阶段,一个Job包含一个或者多个Stage
## Spark 模块
- 以Spark Core为基础,Spark SQL/Stream/graphx/mlib都构建之上
- Spark Core
  - SparkContext内置的DAGScheduler负责创建Job,将DAG中的Job划分成Stage,提交Stage
  - TaskScheduler负责资源的申请、任务的提交与调度
- Spark Sql
  - 增强对Sql以及Hive的支持
  - 将sql转换为语法树,使用规则执行器(RuleExecutor)应用一系列的规则(Rule)到语法树,生成最终的物理执行计划并执行
- Spark Stream
  - 流式计算,DStream为数据流的抽象
- Spark Graphx
  - 分布式图计算框架
- Spark Mib
  - 机器学习
## 集群部署
- Cluster Manager----集群管理器,负责资源的分配与管理
- Worker----Spark工作节点----负责创建Executor,将资源与任务进一步分配给Executor,同步资源信息给Cluster Manager
  - Executor----执行计算任务的进程
- Driver App----客户端驱动程序,将程序任务转换为RDD与DAG,并负责与Cluster Manager进行通信
## 源码分析
- jmx监控
 ```
  -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9999
   -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
 ```
### SparkContext初始化
- 创建SparkEnv
  - org.apache.spark.SparkEnv#create
  - RpcEnv/JavaSerializer/SerializerManager/BroadcastManager/MapOutputTrackerMaster/ShuffleManager/MemoryManager
  - BlockManager/BlockManagerMaster/NettyBlockTransferService
  - MetricsSystem/OutputCommitCoordinator
- 创建SparkUI
  - org.apache.spark.ui.SparkUI#create
  - listenerBus.addListener
  - EnvironmentListener/StorageStatusListener/ExecutorsListener/StorageListener/RDDOperationGraphListener/_jobProgressListener
- 创建_taskScheduler、_schedulerBackend、_dagScheduler
  - org.apache.spark.scheduler.DAGScheduler
    - waitingStages/runningStages/failedStages/activeJobs
### 存储体系
- Driver Program 和 Executor 都会创建BlockManager
  - BlockManager
    - BlockManagerMaster----BlockManagerMasterEndpoint is an ThreadSafeRpcEndpoint on the master node to track statuses of all slaves' block managers.
      - org.apache.spark.storage.BlockManagerMasterEndpoint.receiveAndReply
    - NettyBlockTransferService/ExternalShuffleClient----用于与其他Executor上传/下载数据块(eg:map的中间结果)
      - NettyBlockRpcServer----接收上传/下载Blocks(OpenBlocks/UploadBlock)
      - TransportContext----底层使用netty进行通信
        - org.apache.spark.network.client.TransportClientFactory
        - org.apache.spark.network.TransportContext.createServer
          - TransportServer
            - org.apache.spark.network.server.TransportServer.init
              - io.netty.bootstrap.ServerBootstrap
      - fetchBlocks----org.apache.spark.network.shuffle.OneForOneBlockFetcher.start----client.sendRpc----OpenBlocks
      - uploadBlock----client.sendRpc----new UploadBlock
    - DiskBlockManager---
        - Creates and maintains the logical mapping between logical blocks and physical on-disk
        - locations. One block is mapped to one file with a name given by its BlockId
    - MemoryStore/DiskStore----Actual storage of where blocks are kept