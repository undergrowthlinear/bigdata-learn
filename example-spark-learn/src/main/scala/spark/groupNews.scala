package spark

import org.ansj.splitWord.analysis.ToAnalysis
import org.apache.spark.graphx.Graph
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
object groupNews {

  def main(args: Array[String]): Unit = {

    //相似度阈值
    val sim = 0.2
    val sparkconfig = new SparkConf().setAppName("DocSim").setMaster("local[2]")

    val ctx = new SparkContext(sparkconfig)
    val path = "example-spark-learn\\src\\main\\resources\\te.txt"
    //加载过滤数据(id,title,content)
    val input = ctx.textFile(path).map(x => x.split("====").toSeq).filter(x => x.length == 3 && x(1).length > 1 && x(2).length > 1)

    println("input.count()---->" + input.count())
    //分词(id,title,[words])
    val splitWord = input.map(x => (x(0), x(1), ToAnalysis.parse(x(2)).toStringWithOutNature(" ").split(" ").toSeq))


    //聚类初始化 计算文章向量(id,(id,title,content))
    val init_rdd = input.map(a => {
      (a(0).toLong, a)
    })
    init_rdd.cache()


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


    //构建hashmap索引 ,特征排序取前10个
    val indexArray_pairs = newsIDF.map(a => {
      val indices = a._2.indices
      val values = a._2.values
      val result = indices.zip(values).sortBy(-_._2).take(10).map(_._1)
      (a._1, result)
    })
    //(id,[特征ID])
    indexArray_pairs.cache()


    indexArray_pairs.take(10).foreach(x => println("indexArray_pairs---->" + x._1 + " " + x._2.toSeq))


    //倒排序索引 (词ID,[文章ID])
    val index_idf_pairs = indexArray_pairs.flatMap(a => a._2.map(x => (x, a._1))).groupByKey()


    index_idf_pairs.take(10).foreach(x => println(x._1 + " " + x._2.toSeq))

    //倒排序
    val b_content = index_idf_pairs.collect.toMap
    //广播全局变量
    val b_index_idf_pairs = ctx.broadcast(b_content)
    //广播TF-IDF特征
    val b_idf_parirs = ctx.broadcast(newsIDF.collect.toMap)


    //相似度计算 indexArray_pairs(id,[特征ID]) b_index_idf_pairs( 124971 CompactBuffer(21520885, 21520803, 21521903, 21521361, 21524603))
    val docSims = indexArray_pairs.flatMap(a => {


      //将包含特征的所有文章ID
      var ids: List[Long] = List()

      //存放文章对应的特征
      var idfs: List[(Long, SV)] = List()

      //遍历特征，通过倒排序索引取包含特征的所有文章,除去自身
      a._2.foreach(b => {
        ids = ids ++ b_index_idf_pairs.value.get(b).get.filter(x => (!x.equals(a._1))).map(x => x.toLong).toList
      })


      //b_idf_parirs(tf-idf特征),遍边文章，获取对应的TF-IDF特征
      ids.foreach(b => {
        idfs = idfs ++ List((b, b_idf_parirs.value.get(b.toString).get))
      })

      //获取当前文章TF-IDF特征
      val sv1 = b_idf_parirs.value.get(a._1).get

      import breeze.linalg._
      //构建当前文章TF-IDF特征向量
      val bsv1 = new SparseVector[Double](sv1.indices, sv1.values, sv1.size)
      //遍历相关文章
      val result = idfs.map {
        case (id2, idf2) =>
          val sv2 = idf2.asInstanceOf[SV]
          //对应相关文章的特征向量
          val bsv2 = new SparseVector[Double](sv2.indices, sv2.values, sv2.size)
          //计算余弦值
          val cosSim = bsv1.dot(bsv2) / (norm(bsv1) * norm(bsv2))
          (a._1, id2, cosSim)
      }
      // 文章1，文章2，相似度
      result.filter(a => a._3 >= sim)
    })


    docSims.foreach(x => println("docSims---->" + x))
    //取出所有，有相似度的文章
    val vertexrdd = docSims.map(a => {
      (a._2.toLong, a._1.toLong)
    })


    //构建图
    val graph = Graph.fromEdgeTuples(vertexrdd, 1)
    val graphots = Graph.graphToGraphOps(graph).connectedComponents().vertices

    //聚类初始化 计算文章向量  init_rdd(id,(id,content,title))
    init_rdd.join(graphots).take(10).foreach(x => println("init_rdd.join(graphots)---->"+x))

    val simrdd = init_rdd.join(graphots).map(a => {
      (a._2._2, (a._2._1, a._1))
    })
    val simrddtop = simrdd.groupByKey().filter(a => a._2.size >= 1).sortBy(-_._2.size).take(50)
    val simrdd2 = ctx.parallelize(simrddtop, 18)

    println("simrdd2---->" + simrdd2.count())

    simrdd2.take(10).foreach(x => {
      val titles = x._2.map(x => x._1(1)).toArray
      //选取事件主题名
      val title = mostSimilartyTitle(titles)
      println("事件---------------------" + title)
      println(x._1)
      x._2.foreach(x => println(x._2 + " " + x._1(0) + " " + x._1(1)))


    })


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
