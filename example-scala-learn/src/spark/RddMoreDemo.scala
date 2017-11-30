package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-08-21:47
  * @version 1.0.0
  */
object RddMoreDemo {


  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName(SomeSparkDemo.getClass.getCanonicalName).setMaster("local")
    val sc = new SparkContext(conf)
    val inputFile = "README.md"
    // 创建rdd 集合并行化
    val linesList = sc.parallelize(List(1, 2, 3, 4, 3))
    // 创建rdd 读取外部数据集
    val input = sc.textFile(inputFile)
    pair_rdd_demo(input)
    pair_combine_demo(sc)
  }

  def pair_rdd_demo(input: RDD[String]): Unit = {
    // 通过map 返回元祖 创建 键值对RDD
    val pairRdd = input.map(line => (line.split(" ")(0), line))
    println(input)
    println(pairRdd)
    println(input.count())
    // 通过key 进行操作
    pairRdd.reduceByKey((x, y) => x + y).collect() foreach (println)
    // 进行分组
    pairRdd.groupByKey().collect().foreach(println)
    // 应用值
    pairRdd.mapValues(x => x + " spark").foreach(println)
    // 返回key value sort flat
    pairRdd.keys.foreach(println)
    pairRdd.values.foreach(println)
    pairRdd.sortByKey().foreach(println)
    println(pairRdd.flatMapValues(x => (x.length to 10)).count())
    println("----------------filter----------------")
    pairRdd.filter {
      case (key, value) => value.length < 20
    }.foreach(println)
    println("----------------filter----------------")
    // 统计计数 mapValue返回key-tuple,再进行元组操作
    pairRdd.mapValues(x => (x, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).foreach(println)
    // 求键平均值
    // org.apache.spark.rdd.PairRDDFunctions.combineByKey
    val result = pairRdd.combineByKey(
      (v) => (v, 1),
      (acc: (String, Int), v) => (acc._1 + v, acc._2 + 1),
      (acc1: (String, Int), acc2: (String, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
    )
    result.collectAsMap().map(println(_))
  }

  def pair_combine_demo(sc: SparkContext): Unit = {
    val initialScores = Array(("Fred", 88.0), ("Fred", 95.0), ("Fred", 91.0), ("Wilma", 93.0), ("Wilma", 95.0), ("Wilma", 98.0))
    val d1 = sc.parallelize(initialScores)
    type MVType = (Int, Double) //定义一个元组类型(科目计数器,分数)
    d1.combineByKey(
      score => (1, score),
      (c1: MVType, newScore) => (c1._1 + 1, c1._2 + newScore),
      (c1: MVType, c2: MVType) => (c1._1 + c2._1, c1._2 + c2._2)
    ).map { case (name, (num, socre)) => (name, socre / num) }.collect.foreach(println)
  }

}
