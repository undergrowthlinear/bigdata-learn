# flink 1.5.0学习笔记1之Local WordCount与源码解析
## 参考
- http://flink-china.org/introduction.html
- https://tech.meituan.com/Flink_Benchmark.html
- https://bigdata.163yun.com/product/article/5
```
Flink程序的基础构建模块是 流（streams） 与 转换（transformations）

分布式、高性能、高可用、高精确的数据流应用而生的开源流式处理框架
低延迟、exactly once、流和批统一的，能够支撑足够大体量的复杂计算的引擎

无穷数据集：无穷的持续集成的数据集合
有界数据集：有限不会改变的数据集合

流式：只要数据一直在产生，计算就持续地进行
批处理：在预先定义的时间内运行计算，当完成时释放计算机资源

Flink 是基于直观地去处理无穷数据集的流式运算模型
Flink保证状态化计算强一致性/Flink支持流式计算和带有事件时间语义的视窗/
Flink能满足高并发和低延迟（计算大量数据很快）/
```
## 示例代码
- https://github.com/apache/flink/blob/release-1.5.0/flink-examples/flink-examples-batch/src/main/java/org/apache/flink/examples/java/wordcount/WordCount.java
## 主要概念
### flink分层
- 部署层----支持local/standalone/cloud
- 运行层----基本使用scala编写,分布式数据流支持
- api层,批与流----DataSet/DataStream
### flink 三端
- flink client 程序----提交JobGraph
- flink JobManager----接收JobGraph,进行任务分配给TaskManager
- flink TaskManager----执行TaskManager
### 以批处理WordCount的Local模式为例,简述源码流程
- ExecutionEnvironment.getExecutionEnvironment----根据配置,创建LocalEnvironment
- 当未进行input/output参数传递时,进入org.apache.flink.api.java.DataSet#print
    - org.apache.flink.api.java.DataSet#collect
    - org.apache.flink.api.java.ExecutionEnvironment#execute
    - org.apache.flink.api.java.LocalEnvironment#execute
        - org.apache.flink.api.java.ExecutionEnvironment#createProgramPlan----创建执行计划,使用translator.translateToPlan进行操作转换
            - org.apache.flink.api.java.operators.OperatorTranslation#translate----进行各种操作的转换,封装用户自定义的function
                - eg:---->org.apache.flink.api.java.operators.FilterOperator#translateToDataFlow
        - executor.executePlan----执行转换的计划
            - org.apache.flink.client.LocalExecutor#executePlan
                - org.apache.flink.client.LocalExecutor#start
                - org.apache.flink.client.LocalExecutor#createJobExecutorService
                    - org.apache.flink.runtime.minicluster.MiniCluster#start----创建local集群,创建相关的支撑服务
                        - org.apache.flink.runtime.rpc.RpcService----创建jobManagerRpcService/taskManagerRpcServices/resourceManagerRpcService的rpc服务
                        - org.apache.flink.runtime.rest.RestServerEndpoint#start----创建分发rest服务