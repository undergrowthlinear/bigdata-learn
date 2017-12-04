package spark

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-09-23:04
  * @version 1.0.0
  */
object PartitionerDemo {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName(SomeSparkDemo.getClass.getCanonicalName).setMaster("local")
    val sc = new SparkContext(conf)
    val pairs = sc.parallelize(List((1, 1), (2, 2), (3, 3)))
    println(pairs.partitioner)
    val partitioned = pairs.partitionBy(new HashPartitioner(2))
    println(partitioned.partitioner)

  }
}
