package com.undergrowth;

import com.undergrowth.spark.util.SparkCommonUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

/**
 * Hello world!
 */
public class AppParallelize {

  public static void main(String[] args) {
    SparkConf conf = SparkCommonUtil.getSparkConf("HelloWorld", "local");
    // setMaster指定Master
    // setSparkHome指向安装spark的地址，视环境而定
    JavaSparkContext sc = new JavaSparkContext(conf);

    JavaRDD<String> data = sc.textFile("README.md");
    // 加载README.md文件并创建RDD
    data.foreach(new VoidFunction<String>() {
      public void call(String s) throws Exception {
        System.out.println(s);
      }
    });
    // 输出RDD中的每个分区的内容
  }
}
