package spark.itemcf

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


/**
  * ${Description}
  *
  * @author zhangwu
  * @date 2018-11-15-15:00
  * @version 1.0.0
  */


object BasedItemCFSpark {
  def main(args: Array[String]): Unit = {

    if (args.length != 5) {
      println("BasedItemCFSpark input-inputPath output-inputPath sim  numRepar mode")
      System.exit(1)
    }
    val inputPath = args(0)
    val outPath = args(1)
    //相似度阈值
    val sim = args(2).toFloat
    val numRepar = args(3).toInt
    // local or cluster
    val mode = args(4)

    val sparkConfig = new SparkConf().setAppName("BasedItemCFSpark")
    if (mode != null && mode.equals("local"))
      sparkConfig.setMaster("local[5]")
    val ctx = new SparkContext(sparkConfig)
    //加载过滤数据(user-id,(item-id,score))
    val input = ctx.textFile(inputPath).map(x => x.split(" ").toSeq).filter(s => s.length == 3).map(s => (s.head, (s(1).toInt, s(2).toFloat)))

    // println("input.count()---->" + input.count())
    // 去除没有评分的元素
    val user_visited = input.filter(_._2._2 > 0).combineByKey((v: (Int, Float)) => {
      val list = List[(Int, Float)]()
      v :: list
    }, (x: List[(Int, Float)], v: (Int, Float)) => {
      v :: x
    }, (x: List[(Int, Float)], y: List[(Int, Float)]) => {
      x ::: y
    }).filter(v => v._2.size >= 2).mapValues(v => {
      val flag = v.size > 500
      if (flag) { // 按照评分降序 只取一定数目的行为记录
        v.sortBy(_._2).reverse
      }
      val v_a: mutable.Set[Int] = mutable.Set()
      val v_b: mutable.Set[Int] = if (flag) null else mutable.Set()
      v.foreach(vl => {
        if (!flag) v_a.add(vl._1)
        else v_b.add(vl._1)
      })
      (v_a, v_b)
    })

    user_visited.take(10).foreach(t => println("user_visited---->" + t))

    val b_user_visited = ctx.broadcast(user_visited.collect().toMap)

    val leg_items = input.map(t => (t._2._1, 1)).reduceByKey((a, b) => a + b).filter(t => t._2 > 1).keys

    leg_items.take(10).foreach(t => println("leg_items---->" + t))

    val b_leg_items = ctx.broadcast(leg_items.collect().toSet)
    // 过滤数据
    val data_valid = input.repartition(numRepar).filter(t => b_user_visited.value.contains(t._1) && b_leg_items.value.contains(t._2._1))
    data_valid.cache()
    // println("data_valid count---->" + data_valid.count())
    // 计算item-user/user-item
    val item_user_list = data_valid.map(t => (t._2._1, (t._1, t._2._2))).groupByKey()
    item_user_list.cache()
    item_user_list.take(10).foreach(t => println("item_user_list---->" + t))
    // 计算item-sim
    val item_norm = item_user_list.mapValues(v => {
      var score: Float = 0.0f
      v.foreach(vl => {
        score += vl._2 * vl._2
      })
      score
    })
    val b_item_norm = ctx.broadcast(item_norm.collect().toMap)
    val item_similaries = data_valid.groupByKey().flatMap(t => {
      val item_pair_set: mutable.Set[(ItemPair, Float)] = mutable.Set()
      val item_list = t._2
      item_list.foreach(item => {
        item_list.foreach(inner_item => {
          if (!(item._1 == inner_item._1)) item_pair_set.add((new ItemPair(item._1, inner_item._1), item._2 * inner_item._2))
        })
      })
      item_pair_set
    }).reduceByKey((a, b) => a + b).map(t => {
      val up: ItemPair = t._1
      val norm_a: Float = b_item_norm.value.get(up.x).get
      val norm_b: Float = b_item_norm.value.get(up.y).get
      (up, t._2 / math.sqrt(norm_a * norm_b))
    }).filter(t => t._2 > sim).flatMap(t => {
      val it_list = List()
      (t._1.y, (t._1.x, t._2)) :: (t._1.x, (t._1.y, t._2)) :: it_list
    }).groupByKey().mapValues(v => {
      // 归一化处理
      val v_sort = v.toSeq.sortBy(_._2).reverse
      val max: (Int, Double) = v_sort.head
      val v_sort_chan = v_sort.map(vl => (vl._1, vl._2 / max._2))
      v_sort_chan
    })
    item_similaries.cache()

    item_similaries.take(10).foreach(t => println("item_similaries---->" + t))
    // 利用item_similaries/item_user_list 再加上user_visited 算出用户的top n推荐
    val user_similaries = item_similaries.join(item_user_list).flatMap(t => {
      var u_i_list: List[((String, Int), Double)] = List()
      val users = t._2._2
      val item_sim = t._2._1
      users.foreach(u => {
        val visited = b_user_visited.value.get(u._1).get
        val visited_a = visited._1
        val visited_b = visited._2
        if (visited_b == null || !visited_b.contains(t._1)) { // 有点问题
          item_sim.foreach(it => {
            if (!visited_a.contains(it._1)) { // 不在访问列表中
              // score = it-sim * user-score
              u_i_list = u_i_list ++ List(((u._1, it._1), it._2 * u._2))
            }
          })
        }
      })
      u_i_list
    }).reduceByKey((a, b) => a + b).map(t => (t._1._1, (t._1._2, t._2))).groupByKey().mapValues(v => v.toSeq.sortBy(_._2).reverse)

    println("user_similaries---->" + user_similaries.count())
    user_similaries.take(30).foreach(t => println("user_similaries---->" + t))


    user_similaries.saveAsTextFile(outPath + "\\" + BasedItemCFSpark.getClass.getSimpleName + "_" + System.currentTimeMillis())
  }

}


