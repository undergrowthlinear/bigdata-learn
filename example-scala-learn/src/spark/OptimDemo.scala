package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-13-22:35
  * @version 1.0.0
  */
object OptimDemo {

  def main(args: Array[String]) {
    val logFile = "test.txt" // Should be some file on your system
    val conf = new SparkConf().setAppName(OptimDemo.getClass.getCanonicalName).setMaster("local")
    val sc = new SparkContext(conf)
    val input = sc.textFile(logFile)
    // 切分为单词并且删掉空行
    val tokenized = input.map(line => line.split(" ")).filter(words => words.size > 0)
    // 提取出每行的第一个单词（日志等级）并进行计数
    val counts = tokenized.map(words => (words(0), 1)).reduceByKey { (a, b) => a + b }
    // 调试信息
    println(input.toDebugString)
    println(counts.toDebugString)
    // 行动
    tokenized.collect().foreach(println)
    counts.collect().foreach(println)
    counts.cache()
    counts.collect()
    println()
  }

}
