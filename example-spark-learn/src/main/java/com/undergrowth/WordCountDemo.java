package com.undergrowth;

import com.undergrowth.spark.util.SparkCommonUtil;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 单词计数
 * @date 2017-11-06-21:01
 */
public class WordCountDemo {

  public static void main(String[] args) {
    SparkConf conf = SparkCommonUtil.getSparkConf("HelloWorld", "local");
    // setSparkHome指向安装spark的地址，视环境而定
    JavaSparkContext sc = new JavaSparkContext(conf);

    JavaRDD<String> data = sc.textFile("README.md");
    // 切分为单词
    JavaRDD<String> words = data.flatMap(
        new FlatMapFunction<String, String>() {
          public Iterator<String> call(String x) {
            return Arrays.asList(x.split(" ")).iterator();
          }
        });
// 转换为键值对并计数
    JavaPairRDD<String, Integer> counts = words.mapToPair(
        new PairFunction<String, String, Integer>() {
          public Tuple2<String, Integer> call(String x) {
            return new Tuple2(x, 1);
          }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
      public Integer call(Integer x, Integer y) {
        return x + y;
      }
    });
// 将统计出来的单词总数存入一个文本文件，引发求值
    System.out.println(counts.count());
  }
}
