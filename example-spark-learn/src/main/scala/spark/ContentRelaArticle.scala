package spark

import breeze.linalg.SparseVector
import org.ansj.splitWord.analysis.ToAnalysis
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.mllib.linalg.{SparseVector => SV}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-10-29-17:10
  * @version 1.0.0
  */
object ContentRelaArticle {

  def main(args: Array[String]): Unit = {

    if (args.length != 5) {
      println("ContentRelaArticle input-inputPath output-inputPath sim featureNum numRepar")
      System.exit(1)
    }

    // val inputPath = "E:\\iyourcar\\project\\server-services-bdbox\\sql\\article_id_title_content_head_10000.txt"
    val inputPath = args(0)
    val outPath = args(1)
    //相似度阈值
    val sim = args(2).toFloat
    val featureNum = args(3).toInt
    val numRepar=args(4).toInt
    val sparkConfig = new SparkConf().setAppName("ContentRelaArticle").set("spark.defalut.parallelism", "24")
     // .setMaster("local[5]")
    val ctx = new SparkContext(sparkConfig)
    //加载过滤数据(id,title,content)
    val input = ctx.textFile(inputPath).map(x => x.split("\001").toSeq).filter(x => x.length == 3 && x.head != null && x.head != "\\N" && x(1) != "\\N" && x(2) != "\\N" && x(1).length > 1 && x(2)
      .length > 1)

    //println("input.count()---->" + input.count())
    //分词(id,title,[words])
    val splitWord = input.map(x => (x(0), x(1), ToAnalysis.parse(x(2)).toStringWithOutNature(" ").split(" ").toSeq))


    //聚类初始化 计算文章向量(id,(id,title,content))
    /* val initRdd = input.map(a => {
       (a(0).toLong, a)
     })
     initRdd.cache()*/


    //计算TF-IDF特征值
    val hashingTF = new HashingTF(Math.pow(2, 18).toInt)
    //计算TF
    val newSTF = splitWord.map(x => (x._1, hashingTF.transform(x._3))
    )
    newSTF.cache()
    //构建idf model
    val idf = new IDF().fit(newSTF.values)
    //将tf向量转换成tf-idf向量
    val newsIDF = newSTF.mapValues(v => idf.transform(v)).map(a => (a._1, a._2.toSparse))


    newsIDF.take(10).foreach(x => println("newsIDF---->" + x))


    //构建hashmap索引 ,特征排序取前30个 (id,[tf-idf index])
    val indexIdFeatureArrayPair = newsIDF.map(a => {
      val indices = a._2.indices
      val values = a._2.values
      val result = indices.zip(values).sortBy(-_._2).take(featureNum).map(_._1)
      (a._1, result)
    })
    //(id,[特征ID])
    indexIdFeatureArrayPair.cache()


    indexIdFeatureArrayPair.take(10).foreach(x => println("indexIdFeatureArrayPair---->" + x._1 + " " + x._2.toSeq))


    //倒排序索引 (词ID,[文章ID])
    val inverseFeatureIdArrayPairs = indexIdFeatureArrayPair.flatMap(a => a._2.map(x => (x, a._1))).groupByKey()


    //inverseFeatureIdArrayPairs.take(10).foreach(x => println(x._1 + " " + x._2.toSeq))

    //倒排序
    val b_content = inverseFeatureIdArrayPairs.collect.toMap
    //广播全局变量
    val bInverseFeatureIdArrayPairs = ctx.broadcast(b_content)
    //广播TF-IDF特征
    val bIdfPairs = ctx.broadcast(newsIDF.collect.toMap)


    //相似度计算 indexIdFeatureArrayPair(id,[特征ID]) bInverseFeatureIdArrayPairs( 124971 CompactBuffer(21520885, 21520803, 21521903, 21521361, 21524603))
    val docSims = indexIdFeatureArrayPair.repartition(numRepar).flatMap(a => {


      //将包含特征的所有文章ID
      var ids: Set[Long] = Set()

      //存放文章对应的特征
      //var idfs: List[(Long, SV)] = List()

      var result: List[((Long, Long), (Long, Long))] = List()

      //遍历特征，通过倒排序索引取包含特征的所有文章,除去自身
      // 同时还要包含多个此项中的相同文章id 避免多次计算
      a._2.foreach(b => {
        ids = ids ++ bInverseFeatureIdArrayPairs.value.get(b).get.filter(x => !x.equals(a._1)).map(x => x.toLong).toList
      })


      //bIdfPairs(tf-idf特征),遍边文章，获取对应的TF-IDF特征
      ids.foreach(b => {
        // idfs = idfs ++ List((b, bIdfPairs.value.get(b.toString).get))
        val id2 = b
        val id1=a._1.toInt.asInstanceOf[Number].longValue
        if (a._1.toInt <= id2)
          result = result ++ List(((id1, id2), (id1, id2)))
        else
          result = result ++ List(((id2, id1), (id2, id1)))
      })

      //获取当前文章TF-IDF特征
      //val sv1 = bIdfPairs.value.get(a._1).get

      // import breeze.linalg._
      //构建当前文章TF-IDF特征向量
      //val bsv1 = new SparseVector[Double](sv1.indices, sv1.values, sv1.size)
      //遍历相关文章
      /*val result = idfs.map {
        case (id2, idf2) =>
          //val sv2 = idf2.asInstanceOf[SV]
          //对应相关文章的特征向量
          //val bsv2 = new SparseVector[Double](sv2.indices, sv2.values, sv2.size)
          //计算余弦值
          //val cosSim = bsv1.dot(bsv2) / (norm(bsv1) * norm(bsv2))
          // 将小的放置在前面 后续好过滤
          if (a._1.toInt <= id2)
            ((a._1.toInt, id2), (a._1.toInt, id2))
          else
            ((id2, a._1.toInt), (id2, a._1.toInt))
      }*/
      // 文章1，文章2，相似度
      // result.filter(a => a._3 >= sim)
      result
    })

    val docSimsCal = docSims.groupByKey().distinct().mapValues(it => {
      val id1 = it.toSeq.head._1
      val id2 = it.toSeq.head._2
      val sv1 = bIdfPairs.value.get(id1.toString).get
      val sv2 = bIdfPairs.value.get(id2.toString).get
      //构建当前文章TF-IDF特征向量
      val bsv1 = new SparseVector[Double](sv1.indices, sv1.values, sv1.size)
      //对应相关文章的特征向量
      val bsv2 = new SparseVector[Double](sv2.indices, sv2.values, sv2.size)
      import breeze.linalg._
      val cosSim = bsv1.dot(bsv2) / (norm(bsv1) * norm(bsv2))
      cosSim
    })

    val docSimsCalSort = docSimsCal.flatMap(a => {
      val list = List[(Long, (Long, Double))]()
      val id1: Long = a._1._1.asInstanceOf[Number].longValue
      val id2: Long = a._1._2.asInstanceOf[Number].longValue
      val cos = a._2
      (id1, (id2, cos)) :: (id2, (id1, cos)) :: list
    }).filter(a => a._2._2 >= sim).combineByKey((v: (Long, Double)) => {
      val list = List[(Long, Double)]()
      v :: list
    }, (x: List[(Long, Double)], v: (Long, Double)) => {
      v :: x
    }, (x: List[(Long, Double)], y: List[(Long, Double)]) => {
      x ::: y
    }).mapValues(v => v.sortBy(_._2).reverse)

   // docSimsCalSort.take(10).foreach(a => println(a))

    docSimsCalSort.saveAsTextFile(outPath)

    /*val docSimsInter = docSims.map(a => (a._1, (a._2, a._3))).groupByKey().mapValues(v => v.toSeq.sortBy(_._2).reverse)*/

    /* val docSimsInter = docSims.map(a => (a._1, (a._2, a._3))).combineByKey((v: (Long, Double)) => {
       val list = List[(Long, Double)]()
       v :: list
     }, (x: List[(Long, Double)], v: (Long, Double)) => {
       v :: x
     }, (x: List[(Long, Double)], y: List[(Long, Double)]) => {
       x ::: y
     }).mapValues(v => v.sortBy(_._2).reverse)

     docSimsInter.take(10).foreach(x => println("docSims---->" + x))
     //docSimsInter.saveAsTextFile("E:\\iyourcar\\project\\server-services-bdbox\\sql\\article_id_title_content_head_out.txt")
     docSimsInter.saveAsTextFile(outPath)*/
    //取出所有，有相似度的文章
    /*val vertexrdd = docSims.map(a => {
      (a._2.toLong, a._1.toLong)
    })


    //构建图
    val graph = Graph.fromEdgeTuples(vertexrdd, 1)
    val graphots = Graph.graphToGraphOps(graph).connectedComponents().vertices

    //聚类初始化 计算文章向量  initRdd(id,(id,content,title))
    // initRdd.join(graphots).take(10).foreach(x => println("initRdd.join(graphots)---->" + x))

    val simrdd = initRdd.join(graphots).map(a => {
      (a._2._2, (a._2._1, a._1))
    })
    val simrddtop = simrdd.groupByKey().filter(a => a._2.size >= 1).sortBy(-_._2.size).take(50)
    val simrdd2 = ctx.parallelize(simrddtop, 18)

    // println("simrdd2---->" + simrdd2.count())

    simrdd2.take(10).foreach(x => {
      val titles = x._2.map(x => x._1(1)).toArray
      //选取事件主题名
      val title = mostSimilartyTitle(titles)
      println("事件---------------------" + title)
      println(x._1)
      x._2.foreach(x => println(x._2 + " " + x._1(0) + " " + x._1(1)))


    })*/


  }


  /**
    * 相似度比对 最短编辑距离
    *
    * @param s
    * @param t
    * @return
    */
  def ld(s: String, t: String): Int = {
    var sLen: Int = s.length
    var tLen: Int = t.length
    var cost: Int = 0
    var d = Array.ofDim[Int](sLen + 1, tLen + 1)
    var ch1: Char = 0
    var ch2: Char = 0
    if (sLen == 0)
      tLen
    if (tLen == 0)
      sLen
    for (i <- 0 to sLen) {
      d(i)(0) = i
    }
    for (i <- 0 to tLen) {
      d(0)(i) = i
    }
    for (i <- 1 to sLen) {
      ch1 = s.charAt(i - 1)
      for (j <- 1 to tLen) {
        ch2 = t.charAt(j - 1)
        if (ch1 == ch2) {
          cost = 0
        } else {
          cost = 1
        }
        d(i)(j) = Math.min(Math.min(d(i - 1)(j) + 1, d(i)(j - 1) + 1), d(i - 1)(j - 1) + cost)
      }
    }
    return d(sLen)(tLen)
  }

  /**
    *
    * @param src
    * @param tar
    * @return
    */
  def similarity(src: String, tar: String): Double = {
    val a: Int = ld(src, tar)
    1 - a / (Math.max(src.length, tar.length) * 1.0)
  }


  /**
    * 选出一组字符串 中相似度最高的
    *
    * @param strs
    * @return
    */
  def mostSimilartyTitle(strs: Array[String]): String = {
    var map: Map[String, Double] = Map()
    for (i <- 0 until strs.length) {
      for (j <- i + 1 until strs.length) {
        var similar = similarity(strs(i), strs(j))
        if (map.contains(strs(i)))
          map += (strs(i) -> (map.get(strs(i)).get + similar))
        else
          map += (strs(i) -> similar)
        if (map.contains(strs(j)))
          map += (strs(j) -> (map.get(strs(j)).get + similar))
        else
          map += (strs(j) -> similar)
      }
    } //end of for
    if (map.size > 0)
      map.toSeq.sortWith(_._2 > _._2)(0)._1
    else
      ""
  }

}
