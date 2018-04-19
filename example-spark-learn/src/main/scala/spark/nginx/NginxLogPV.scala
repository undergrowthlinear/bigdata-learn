package spark.nginx

;

/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-01-30-14:06
  * @version 1.0.0
  */

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Created by c on 2017/1/11.
  * 通过nginx日志统计每日pv，并按照日期和pv排序
  * by me:
  * 我本沉默是关注互联网以及分享IT相关工作经验的博客，
  * 主要涵盖了操作系统运维、计算机编程、项目开发以及系统架构等经验。
  * 博客宗旨：把最实用的经验，分享给最需要的你，
  * 希望每一位来访的朋友都能有所收获！
  *
  */
object NginxLogPV {
  /**
    * 设置需要统计的页面
    */
  val pages = new mutable.HashSet[String]()
  pages += ".php"

  /**
    * 封装KPI实体类
    *
    * @param line
    * @return KPI
    */
  def parser(line: String): KPI = {
    //
    val fields = line.split(" ")
    val remote_addr = fields(0)
    val time_local = fields(3).substring(1)
    val request = fields(6)
    val status = fields(8)
    var valid = true
    if (fields.length <= 11) {
      valid = false
    } else {
      valid = if (status.toInt >= 400) false else true
    }
    val url = if (request.indexOf("?") != -1) request.substring(0, request.indexOf("?")) else request
    KPI(remote_addr, time_local, url, status, valid)
  }

  /**
    * 过滤无效数据
    *
    * @param line
    * @return
    */
  def filterPVs(line: String): KPI = {
    val kpi: KPI = parser(line)
    /**
      * 过滤需要统计的URL
      */
    kpi.valid = false
    for (page <- pages) {
      if (kpi.request != null) {
        if (kpi.request.contains(page)) {
          kpi.valid = true
        }
      }
    }
    return kpi;
  }

  /**
    * 将nginx日志时间转换为常规日期
    *
    * @param time_local
    * @return
    */
  def getTime_local_Date(time_local: String): Date = {
    val df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US)
    df.parse(time_local)
  }

  /**
    * 日期格式化
    *
    * @param time_local
    * @return
    */
  def getTime_local_day(time_local: String): String = {
    val df = new SimpleDateFormat("yyyy-MM-dd");
    df.format(getTime_local_Date(time_local));
  }

  def main(args: Array[String]): Unit = {
    // StreamingExamples.setStreamingLogLevels()
    val conf = new SparkConf().setAppName("NginxLogPV").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("E:\\java_other\\bigdata\\spark\\data\\access.log").map(x => {
      /**
        * 封装并过滤数据
        */
      filterPVs(x)
    }).filter(x => {
      /**
        * 过滤有效数据
        */
      x.valid
    })
    val timeRequestRdd =rdd.map(x => {
      /**
        * 封装 key-value数据
        */
      ((getTime_local_day(x.time_local), x.request), 1)
    }).reduceByKey(_ + _) //聚合
    /**
      * 二次排序
      */
    val rdd6 = timeRequestRdd.sortBy(x => PVSort(x._1._1, x._2))
    /**
      * 格式化数据并输出到磁盘
      */
    rdd6.map(x => {
      x._1._1 + "\t" + x._1._2 + "\t" + x._2
    }).saveAsTextFile("E:\\java_other\\bigdata\\spark\\data\\time_requestRdd")
    //
    val timeIpRdd =rdd.map(x => {
      /**
        * 封装 key-value数据
        */
      ((getTime_local_day(x.time_local), x.remote_addr), 1)
    }).reduceByKey(_ + _) //聚合
    /**
      * 二次排序
      */
    val rdd7 = timeIpRdd.sortBy(x => PVSort(x._1._1, x._2))
    /**
      * 格式化数据并输出到磁盘
      */
    rdd7.map(x => {
      x._1._1 + "\t" + x._1._2 + "\t" + x._2
    }).saveAsTextFile("E:\\java_other\\bigdata\\spark\\data\\time_IpRdd")
     //println(rdd6.collect().toBuffer)
    sc.stop()
  }
}

/**
  * 自定义排序，日期升序，点击量降序
  *
  * @param date
  * @param count
  */
case class PVSort(date: String, count: Int) extends Ordered[PVSort] with Serializable {
  override def compare(that: PVSort): Int = {
    val i = this.date.compareTo(that.date)
    if (i == 0) {
      return -this.count.compareTo(that.count)
    } else {
      return i
    }
  }
}

/**
  * kpi样例类
  *
  * @param remote_addr
  * @param time_local
  * @param request
  * @param status
  * @param valid
  */
case class KPI(
                remote_addr: String, //来访ip
                time_local: String, //来访时间
                request: String, //受访页面
                status: String, //状态
                var valid: Boolean = true //判断是否合法
              ) extends Serializable
