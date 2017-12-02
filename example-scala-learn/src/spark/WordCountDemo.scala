package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-06-21:21
  * @version 1.0.0
  */
object WordCountDemo {
  def main(args: Array[String]) {
    val inputFile="README.md"
    val outputFile="./countWord"
    // 创建一个Scala版本的Spark Context
    val conf = new SparkConf().setAppName("wordCount").setMaster("local")
    val sc = new SparkContext(conf)
    // 读取我们的输入数据
    val input = sc.textFile(inputFile)
    // 把它切分成一个个单词
    val words = input.flatMap(line => line.split(" "))
    // 转换为键值对并计数
    val counts = words.map(word => (word, 1)).reduceByKey{case (x, y) => x + y}
    // 将统计出来的单词总数存入一个文本文件，引发求值
    counts.saveAsTextFile(outputFile)
  }
}
