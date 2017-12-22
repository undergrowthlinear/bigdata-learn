# spark 2.2.0学习笔记3之SparkRDDDemo
## Info
### 键值对RDD(pair RDD)----用来进行聚合运算/元素为元组
- reduceByKey----通过key 进行操作----返回新的rdd
- groupByKey----进行分组
- mapValues/flatMapValues----应用值
- keys/values/sortByKey----返回key value sort
- join----对两个rdd进行内连接
- subtractByKey/rightOuterJoin/leftOuterJoin/cogroup
### 操作----转换操作(转换操作返回的是RDD)与行动操作(行动操作返回的是其他类型)
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
## Code
```
package spark30.basic

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-07-21:48
  * @version 1.0.0
  */
object SparkRDDDemo {


  def demo_basic(lines: RDD[String], input: RDD[String]) = {
    println(lines)
    println(input)
    // 转换操作 转换数据集
    val errorRdd=input.filter(line=>line.contains("error"))
    val debugRdd=input.filter(line=>line.contains("debug"))
    val errorUnionDebug=errorRdd.union(debugRdd)
    println(errorRdd+"\t"+debugRdd+"\t"+errorUnionDebug)
    // 行为操作
    println("error info count:"+errorRdd.count()+"\t first error info is:"+errorRdd.first()+
      "\t debug info count:"+debugRdd.count()
      +"\t first debug info is:"+debugRdd.first())
    errorUnionDebug.take(2).foreach(println)
    println("random string")
    errorUnionDebug.take(2).foreach(println)
  }

  def demo_map(input2: RDD[Int]): Unit = {
    val result = input2.map(x => x * x)
    println(result.collect().mkString(","))
  }

  def demo_flatMap(input3: RDD[String]): Unit = {
    val words = input3.flatMap(line => line.split(" "))
    println(words.first()) // 返回"hello"
    words.foreach(println)
  }

  def demo_reduce(input2: RDD[Int]): Unit = {
    var sum=input2.reduce((x,y)=>x+y)
    println("sum is:"+sum)
  }

  def demo_aggregate(input2: RDD[Int]): Unit = {
    // aggregate(zeroValue)(seqOp, combOp)
    // 牛逼
    val result = input2.aggregate((0, 0))(
      (acc, value) => (acc._1 + value, acc._2 + 1),
      (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2))
    val avg = result._1 / result._2.toDouble
    println(result._1+"\t"+result._2.toDouble+"\t"+avg)
  }

  def main(args: Array[String]) {
    // 获取SparkContext
    val sc = SparkContextUtil.getSparkContext(SparkRDDDemo.getClass.getCanonicalName)
    val inputFile="README.md"
    // 创建rdd 集合并行化
    val lines = sc.parallelize(List("pandas", "i like pandas"))
    // 创建rdd 读取外部数据集
    val input = sc.textFile(inputFile)
    demo_basic(lines,input)
    // map 演示
    val input2 = sc.parallelize(List(1, 2, 3, 4))
    demo_map(input2)
    val input3 = sc.parallelize(List("hello world", "hi"))
    demo_flatMap(input3)
    // reduce 演示
    demo_reduce(input2)
    // aggregate
    demo_aggregate(input2)
  }
}

```
# Code
- https://github.com/undergrowthlinear/bigdata-learn.git
    - spark30.basic.SparkRDDDemo