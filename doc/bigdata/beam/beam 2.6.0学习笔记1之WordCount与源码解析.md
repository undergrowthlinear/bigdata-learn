# apache beam 2.6.0学习笔记1之WordCount与源码解析
## 参考
- https://beam.apache.org/get-started/quickstart-java/
- https://blog.csdn.net/ffjl1985/article/details/78055152
- http://www.infoq.com/cn/articles/apache-beam-in-practice
- http://shiyanjun.cn/archives/1567.html
```
Apache Beam的编程范式借鉴了函数式编程的概念、数据处理平台
数据源类型----有界的数据集和无界的数据流
          ----批处理和流处理
时间----有序/乱序
优点----统一（UNIFIED）/可移植（PORTABLE）/可扩展（EXTENSIBLE）
```
## 示例代码
- https://github.com/undergrowthlinear/bigdata-learn/tree/dev/example-beam-learn
- 或者从官方下载 
```
mvn archetype:generate -DarchetypeGroupId=org.apache.beam -DarchetypeArtifactId=beam-sdks-java-maven-archetypes-examples -DarchetypeVersion=2.6.0 -DgroupId=org.example -DartifactId=word-count-beam -Dversion="0.1"      -Dpackage=org.apache.beam.examples -DinteractiveMode=false
```
- 对比官网修改如下
    - MinimalWordCount的read使用绝对路径传递数据源
    - DebuggingWordCount因为数据源不同,修改getFilterPattern与PAssert.that的结果判断
    - WindowedWordCount的WriteOneFilePerWindow中因为window文件名不支持:,故修改为FORMATTER = ISODateTimeFormat.basicTimeNoMillis()即可
    - UserScore在resources下添加数据源user_score.txt验证流程
## 主要概念
### beam 处理流程(3各方面)
- Model----为数据来源的IO,它是由多种数据源或仓库的IO组成,数据源支持批处理和流处理
    - What(如何对数据进行计算/sum..)
    - Where(数据在什么范围中计算/基于 Process-Time 的时间窗口、基于 Event-Time 的时间窗口、滑动窗口)
    - When(何时输出计算结果/ Watermark 和触发器)
    - How(迟到数据如何处理/ Accumulation 指定) 
- Pipeline----管道只有一条，它的作用是连接数据和Runtimes平台
- Runtimes----大数据计算或处理平台,目前支持Apache Flink、Apache Spark、Direct Pipeline 和 Google Clound Dataflow、ApexRunner
### 编程流程
- 创建Pipeline对象并设置Pipeline执行选项，包括Pipeline的执行引擎。
- 为Pipeline创建初始数据集PCollection，使用Source API从外部源读取数据，或使用CreateTransform从内存数据构建PCollection。
- 应用Transform到每个PCollection。Transform可以改变、过滤、分组、分析或以其他方式处理PCollection中的元素。Transform创建一个新的输出PCollection，而不改变输入集合（函数式编程特性）。 典型的Pipeline依次将后续的Transform应用于每个新的输出PCollection，直到处理完成。
- 输出最终的转换PCollection，一般使用Sink API将数据写入外部源。
- 使用指定的执行引擎运行Pipeline代码。
### 编程常见操作
- PipelineOptions----用户可扩展此接口,用于自定义属性,利用jackson去序列/反序列化属性值
- PCollection----数据集合
- PTransform----对PCollection进行转换(think of PCollections as variables and PTransforms as functions applied to these variables: the shape of the pipeline can be an arbitrarily complex processing graph)
- Pipeline----数据处理流水线(manages a directed acyclic graph of PTransforms and PCollections that is ready for execution)
- PipelineRunner----管道运行器(specifies where and how the pipeline should execute)
- ParDo----Beam中表示并行执行的对象，一般会内嵌一个DoFn
- DoFn----Beam中ParDo中编写业务逻辑的对象 
    - 使用注解ProcessElement进行表示方法
    - DoFnInvoker----DoFn的回调机制
- MapElements----映射方法在每一个PCollection元素上
- FlatMapElements----扁平化PCollection中的元素
- Combine----根据key对PCollection中的数据进行组合处理
    - CombineFn使用累加器对输入数据进行处理
- Filter----过滤PCollection中的元素
- Count----对每一个PCollection的元素计数
- TextIO.write/TextIO.read----读写操作
### 以WordCount为入口,简述源码流程
- 启动传递 --inputFile=输入数据源绝对路径 --output=输出数据目录
- org.apache.beam.examples.WordCount.runWordCount
    - Pipeline.create(options)----通过默认的参数,DirectRunner创建管道
    - 读取----TextIO.read()---Read也是一个PTransform
    - 进行单词拆分/与计数
        - org.apache.beam.examples.WordCount.CountWords.expand
            - 利用DoFn的processElement机制,处理读取的元素
            - 利用Count.perElement进行计数(实际上生成org.apache.beam.sdk.transforms.Count.PerElement的PTransform)
                - org.apache.beam.sdk.transforms.Count.PerElement.expand
                    - 对每一个元素进行MapElements转换后,称为KV.of(element, (Void) null)格式
                    - 再进行Count.perKey,实际上是产生Combine.perKey,回调CountFn进行key的累加,完成key的计数
            - 利用MapElements对输入数据利用SimpleFunction进行格式转换
            - 通过TextIO.write对输出数据
    - p.run().waitUntilFinish----等待结果运行完成
        - org.apache.beam.sdk.Pipeline.run
        - org.apache.beam.runners.direct.DirectRunner.run
        - org.apache.beam.runners.direct.ExecutorServiceParallelExecutor.start----使用并行的执行器执行管道
            - numTargetSplits对输入进行划分,创建QuiescenceDriver调用其drive,进行addWorkIfNecessary的提交
            - 提交CommittedBundle到ExecutorServiceParallelExecutor.process,创建TransformExecutor(DirectTransformExecutor)进而创建
            - org.apache.beam.runners.direct.DirectTransformExecutor.run创建TransformEvaluator进而回调
                - org.apache.beam.runners.direct.DoFnLifecycleManagerRemovingTransformEvaluator.processElement
                - org.apache.beam.repackaged.beam_runners_direct_java.runners.core.SimplePushbackSideInputDoFnRunner.processElementInReadyWindows
                - org.apache.beam.repackaged.beam_runners_direct_java.runners.core.SimpleDoFnRunner.processElement
                - 最终回调org.apache.beam.sdk.transforms.reflect.DoFnInvoker.invokeProcessElement执行业务逻辑