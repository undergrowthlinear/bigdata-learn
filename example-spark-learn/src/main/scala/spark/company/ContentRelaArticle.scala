package spark.company

import breeze.linalg.SparseVector
import org.ansj.splitWord.analysis.ToAnalysis
import org.apache.spark.mllib.feature.{HashingTF, IDF}
import org.apache.spark.mllib.linalg.{SparseVector => SV}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * ${Description}
  * 与ContentRelaArticleByCpu相比 先过滤后计算 以内存为代价换取cpu
  *
  * @author zhangwu
  * @date 2018-10-29-17:10
  * @version 1.0.0
  */
object ContentRelaArticle {

  def main(args: Array[String]): Unit = {

    if (args.length != 6) {
      println("ContentRelaArticle input-inputPath output-inputPath sim featureNum numRepar mode")
      System.exit(1)
    }

    val inputPath = args(0)
    val outPath = args(1)
    //相似度阈值
    val sim = args(2).toFloat
    val featureNum = args(3).toInt
    val numRepar = args(4).toInt
    // local or cluster
    val mode = args(5)
    val sparkConfig = new SparkConf().setAppName("ContentRelaArticle").set("spark.defalut.parallelism", "24")
    if (mode != null && mode.equals("local"))
      sparkConfig.setMaster("local[5]")
    val ctx = new SparkContext(sparkConfig)
    //加载过滤数据(id,title,content)
    val input = ctx.textFile(inputPath).map(x => x.split("\001").toSeq).filter(x => x.length == 3 && x.head != null && x.head != "\\N" && x(1) != "\\N" && x(2) != "\\N" && x(1).length > 1 && x(2)
      .length > 1)

    println("input.count()---->" + input.count())
    //分词(id,title,[words])
    val splitWord = input.map(x => (x(0), x(1), ToAnalysis.parse(x(2)).toStringWithOutNature(" ").split(" ").toSeq))


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
        val id1 = a._1.toInt.asInstanceOf[Number].longValue
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

  }

}
