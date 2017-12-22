# spark 2.2.0学习笔记2之LocalWordCountDemo
## Info
### 底层----集群管理器
- local
- 独立调度器----Spark自带/默认
- Hadoop YARN
- Apache Mesos
### 大致源码
- org.apache.spark.SparkContext#createTaskScheduler
    - 根据master创建TaskScheduler与SchedulerBackend
    - 创建DAGScheduler
    - 启动_taskScheduler.start
    - 通知一系列监听器 
## Code
```
package spark30.basic

import org.apache.spark.SparkConf

/**
  * @description
  * @author zhangwu
  * @date 2017-11-06-21:21
  * @version 1.0.0
  */
object LocalWordCountDemo {
  def main(args: Array[String]) {
    val inputFile = "README.md"
    val outputFile = "./countWord"
    // 创建一个Scala版本的Spark Context
    val sc = SparkContextUtil.getSparkContext("wordCount")
    // 读取我们的输入数据
    val input = sc.textFile(inputFile)
    // 把它切分成一个个单词
    val words = input.flatMap(line => line.split(" "))
    // 转换为键值对并计数
    val counts = words.map(word => (word, 1)).reduceByKey { case (x, y) => x + y }
    // 将统计出来的单词总数存入一个文本文件，引发求值
    counts.saveAsTextFile(outputFile)
    sc.stop()
  }
}

```
```
package spark30.basic

import org.apache.spark.{SparkConf, SparkContext}

/**
  * ${Description}
  * SparkContext 工具类,以函数式编程的思维思考问题
  * 集群管理器
  *- local
  *- 独立调度器----Spark自带/默认
  *- Hadoop YARN
  *- Apache Mesos
  *
  * @author zhangwu
  * @date 2017-12-19-13:57
  * @version 1.0.0
  */


object SparkContextUtil {

  val master: String = "local"

  def getSparkContext(appName: String): SparkContext = {
    getSparkContext(appName, master)
  }

  def getSparkContext(appName: String
                      , master: String
                     ): SparkContext = {
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val sc = new SparkContext(conf)
    sc
  }

}

```
# Code
- https://github.com/undergrowthlinear/bigdata-learn.git
    - spark30.basic.LocalWordCountDemo