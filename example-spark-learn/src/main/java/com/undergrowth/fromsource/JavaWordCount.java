package com.undergrowth.fromsource;

import java.util.Arrays;
import java.util.List;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

/**
 * wordcount
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-04-28-17:41
 */
public class JavaWordCount {

    public static void main(String[] args) {
        // 检查参数
        if (args.length < 1) {
            System.err.println("Usage:JavaWordCount <file>");
            System.exit(1);
        }
        // 1. 创建session
        SparkSession spark = SparkSession.builder().appName(JavaWordCount.class.getSimpleName()).master("local").getOrCreate();
        // 读取文件
        JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();
        // 转换为单词
        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(s.split(" ")).iterator());
        // 转换为key-value
        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));
        // 计数
        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);
        // rdd的行为操作 开始计算
        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<String, Integer> tuple2 :
            output) {
            System.out.println(tuple2._1() + ":" + tuple2._2());
        }
        spark.close();
    }

}