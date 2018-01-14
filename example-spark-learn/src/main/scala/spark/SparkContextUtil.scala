package spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * ${Description}
  * SparkContext 工具类,以函数式编程的思维思考问题
  * 集群管理器
  * - local
  * - 独立调度器----Spark自带/默认
  * - Hadoop YARN
  * - Apache Mesos
  *
  * @author zhangwu
  * @date 2017-12-19-13:57
  * @version 1.0.0
  */


object SparkContextUtil {

  val master: String = "local"
  val master2: String = "local[2]"

  def getSparkSession(appName: String): SparkSession = {
    SparkSession
      .builder()
      .appName(appName)
      .master(master)
      .getOrCreate()
  }

  def getSparkSession(master: String, appName: String): SparkSession = {
    SparkSession
      .builder()
      .appName(appName)
      .master(master).config("spark.executor.memory", "512M").config("spark.executor.cores", "1")
      .getOrCreate()
  }

  def getStreamingContext(appName: String): StreamingContext = {
    val sparkConf = new SparkConf().setAppName(appName).setMaster(master2)
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc
  }

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
