package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-07-21:48
  * @version 1.0.0
  */
object SomeSparkDemo {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName(SomeSparkDemo.getClass.getCanonicalName).setMaster("local")
    val sc = new SparkContext(conf)
    val inputFile="README.md"
    // 创建rdd 集合并行化
    val lines = sc.parallelize(List("pandas", "i like pandas"))
    // 创建rdd 读取外部数据集
    val input = sc.textFile(inputFile)
    println(lines)
    println(input)
  }
}
