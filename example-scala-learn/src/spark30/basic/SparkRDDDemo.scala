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
