package spark

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * @description
  * @author zhangwu
  * @date 2017-11-09-23:24
  * @version 1.0.0
  */
object PageRank {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName(SomeSparkDemo.getClass.getCanonicalName).setMaster("local")
    val sc = new SparkContext(conf)
    // 假设相邻页面列表以Spark objectFile的形式存储
    val links = sc.objectFile[(String, Seq[String])]("links")
      .partitionBy(new HashPartitioner(100))
      .persist()
    // 将每个页面的排序值初始化为1.0；由于使用mapValues，生成的RDD
    // 的分区方式会和"links"的一样
    var ranks = links.mapValues(v => 1.0)
    // 运行10轮PageRank迭代
    for(i <- 0 until 10) {
      val contributions = links.join(ranks).flatMap {
        case (pageId, (links, rank)) =>
          links.map(dest => (dest, rank / links.size))
      }
      ranks = contributions.reduceByKey((x, y) => x + y).mapValues(v => 0.15 + 0.85*v)
    }
    // 写出最终排名
    ranks.saveAsTextFile("ranks")
  }
}
