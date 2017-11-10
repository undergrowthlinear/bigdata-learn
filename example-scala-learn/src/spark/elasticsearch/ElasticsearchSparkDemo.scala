package spark.elasticsearch

import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.EsSpark
import org.elasticsearch.spark.rdd.Metadata._


/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2017-11-10-15:38
  * @version 1.0.0
  */

object ElasticsearchSparkDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local")
    val sc = new SparkContext(conf)
    // 主机/端口 已在conf文件配置
    conf.set("es.index.auto.create", "true")
    val numbers = Map("one" -> 1, "two" -> 2, "three" -> 3)
    val airports = Map("OTP" -> "Otopeni", "SFO" -> "San Fran")
    sc.makeRDD(Seq(numbers, airports)).saveToEs("test/spark")
    //
    val upcomingTrip = Trip("OTP", "SFO")
    val lastWeekTrip = Trip("MUC", "OTP")
    val rdd = sc.makeRDD(Seq(upcomingTrip, lastWeekTrip))
    rdd.saveToEs("test/sparkclass")
    // 显示写入
    val rddDirect = sc.makeRDD(Seq(upcomingTrip, lastWeekTrip))
    EsSpark.saveToEs(rddDirect, "test/sparkclass2")
    // json 写入
    val json1 = """{"id" : 1, "blog" : "www.iteblog.com", "weixin" : "iteblog_hadoop"}"""
    val json2 = """{"id" : 2, "blog" : "books.iteblog.com", "weixin" : "iteblog_hadoop"}"""
    sc.makeRDD(Seq(json1, json2)).saveJsonToEs("test/sparkjson")
    // type是通过{media_type}通配符设置的
    val game = Map("media_type"->"game","title" -> "FF VI","year" -> "1994")
    val book = Map("media_type" -> "book","title" -> "Harry Potter","year" -> "2010")
    val cd = Map("media_type" -> "music","title" -> "Surfing With The Alien")
    sc.makeRDD(Seq(game, book, cd)).saveToEs("test/spark_{media_type}")
    // Seq((1, otp), (2, muc), (3, sfo))语句指定为各个对象指定了id值，分别为1、2、3
    val otp = Map("iata" -> "OTP", "name" -> "Otopeni")
    val muc = Map("iata" -> "MUC", "name" -> "Munich")
    val sfo = Map("iata" -> "SFO", "name" -> "San Fran")
    val airportsRDD = sc.makeRDD(Seq((1, otp), (2, muc), (3, sfo)))
    airportsRDD.saveToEsWithMeta("test/spark2015")
    // 显示指定映射信息
    val json1 = """{"id" : 1, "blog" : "www.iteblog.com", "weixin" : "iteblog_hadoop"}"""
    val json2 = """{"id" : 2, "blog" : "books.iteblog.com", "weixin" : "iteblog_hadoop"}"""
    val rddMapping = sc.makeRDD(Seq(json1, json2))
    EsSpark.saveToEs(rddMapping, "test/sparkmapping", Map("es.mapping.id" -> "id"))
    //
    val otp2 = Map("iata" -> "OTP", "name" -> "Otopeni")
    val muc2 = Map("iata" -> "MUC", "name" -> "Munich")
    val sfo2 = Map("iata" -> "SFO", "name" -> "San Fran")
    val otpMeta = Map(ID -> 1, TTL -> "3h")
    val mucMeta = Map(ID -> 2, VERSION -> "23")
    val sfoMeta = Map(ID -> 3)
    val airportsRDD2 = sc.makeRDD(Seq((otpMeta, otp), (mucMeta, muc), (sfoMeta, sfo)))
    airportsRDD2.saveToEsWithMeta("test/spark2015")
    // 读取数据
    // by default, as a Tuple2 containing as the first element the document id and the second element the actual document represented through Scala collections
    val RDD = sc.esRDD("test/spark")
    val RDD2=sc.esRDD("test/spark", "?q=me*")
  }

}

case class Trip(departure: String, arrival: String)
