package spark.rec

import spark30.basic.SparkContextUtil


/**
  * ${Description}
  * 参考----https://www.cnblogs.com/muchen/p/6882465.html
  * 基于ALS矩阵分解算法的Spark推荐引擎实现
  * MovieLens数据集
  *
  * @author zhangwu
  * @date 2017-12-25-10:38
  * @version 1.0.0
  */
object SparkAlsMLRecDemo {

  def main(args: Array[String]): Unit = {
    // 1----载入用户对影片的评级数据
    val sc = SparkContextUtil.getSparkContext(SparkAlsMLRecDemo.getClass.getCanonicalName)
    // 载入评级数据
    val rawData = sc.textFile("doc\\spark\\data\\u.data")
    // 展示一条记录
    println(rawData.first())
    // 2----切分记录并返回新的RDD
    // 格式化数据集
    val rawRatings = rawData.map(_.split("\t").take(3))
    // 展示一条记录
    println(rawRatings.first())
    // 3----接下来需要将评分矩阵RDD转化为Rating格式的RDD
    // 导入rating类
    import org.apache.spark.mllib.recommendation.Rating
    // 将评分矩阵RDD中每行记录转换为Rating类型
    val ratings = rawRatings.map { case Array(user, movie, rating) => Rating(user.toInt, movie.toInt, rating.toDouble) }
    //-------------------------------------训练
    // 导入ALS推荐系统算法包
    import org.apache.spark.mllib.recommendation.ALS
    // 启动ALS矩阵分解
    val model = ALS.train(ratings, 50, 10, 0.01)
    println(model)
    //-------------------------------------预测
    val userId = 789
    val K = 10
    // 获取推荐列表
    val topKRecs = model.recommendProducts(userId, K)
    // 打印推荐列表
    println(userId + "的推荐列表")
    println(topKRecs.mkString("\n"))
    //-------------------------------------检验推荐效果
    // 导入电影数据集
    val movies = sc.textFile("doc\\spark\\data\\u.item")
    // 建立电影id - 电影名字典
    val titles = movies.map(line => line.split("\\|").take(2)).map(array => (array(0).toInt, array(1))).collectAsMap()
    // 查看id为123的电影名
    println(titles(123))
    // 建立用户名-其他RDD，并仅获取用户789的记录
    val moviesForUser = ratings.keyBy(_.user).lookup(789)
    // 获取用户评分最高的10部电影，并打印电影名和评分值
    moviesForUser.sortBy(-_.rating).take(10).map(rating => (titles(rating.product), rating.product, rating.rating)).foreach(println)
    //
    println(userId + "的推荐列表")
    println(topKRecs.mkString("\n"))
  }

}
