package basic

;

/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-11-12-19:42
  * @version 1.0.0
  */

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hjw on 17/9/11.
  *
  **/
/*
event uuid  pv data.txt的数据
1	    1001	0
1	    1001	1
1	    1002	0
1	    1003	1
2	    1002	1
2	    1003	1
2	    1003	0
3	    1001	0
3	    1001	0
计算 event,count(distinct if(pv > 0,uuid,null)) ,sum(pv)
2	 UV=2	 PV=2
3	 UV=0	 PV=0
1	 UV=2	 PV=2
为了分析,将map和reduce没有写成链式
 */
object DAG {

  def main(args: Array[String]) {
    val conf = new SparkConf()

    conf.setAppName("test")
    conf.setMaster("local")

    val sc = new SparkContext(conf)

    val txtFile = sc.textFile("E:\\iyourcar\\project\\server-services-bdbox\\sql\\dagData.txt")
    val inputRDD = txtFile.map(x => (x.split("\t")(0), x.split("\t")(1), x.split("\t")(2).toInt))
    //val partitionsSzie = inputRDD.partitions.size

    //这里为了分析task数先重分区,分区前partitions.size = 1,下面每个stage的task数为1
    val inputPartionRDD = inputRDD.repartition(2)

    //------map_shuffle stage 有shuffle Read
    //结果:(事件-用户,pv)
    val eventUser2PV = inputPartionRDD.map(x => (x._1 + "-" + x._2, x._3))

    //结果: (事件,(用户,pv))
    val PvRDDTemp1 = eventUser2PV.reduceByKey(_ + _).map(x =>
      (x._1.split("-")(0), (x._1.split("-")(1), x._2))
    )

    //-------map_shuffle stage   有shuffle Read 和 有shuffle Write
    //结果: (事件, Tuple2(Tuple2(用户,是否出现),该用户的pv) )
    val PvUvRDDTemp2 = PvRDDTemp1.map(
      x => x match {
        case x if x._2._2 > 0 => (x._1, (1, x._2._2))
        case x if x._2._2 == 0 => (x._1, (0, x._2._2))
      }
    )

    //结果:(事件,Tuple2(uv,pv))
    val PVUVRDD = PvUvRDDTemp2.reduceByKey(
      (a, b) => (a._1 + b._1, a._2 + b._2)
    )

    //------result_shuffle stage 有shuffle Read
    //--------触发一个job
    val res = PVUVRDD.collect();


    //------result_shuffle stage 有shuffle Read
    //--------触发一个job
    PVUVRDD.foreach(a => println(a._1 + "\t UV=" + a._2._1 + "\t PV=" + a._2._2))
    //    2	 UV=2	 PV=2
    //    3	 UV=0	 PV=0
    //    1	 UV=2	 PV=2
    while (true) {
      Thread.sleep(10000)
    }
    sc.stop()
  }
}
