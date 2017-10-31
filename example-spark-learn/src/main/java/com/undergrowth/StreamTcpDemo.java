package com.undergrowth;

import com.undergrowth.spark.util.SparkCommonUtil;
import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

/**
 * 流演示
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-10-31-10:36
 */
public class StreamTcpDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create a local StreamingContext with two working thread and batch interval of 1 second
        SparkConf conf = SparkCommonUtil.getSparkConf("NetworkWordCount", "local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
        // Create a DStream that will connect to hostname:port, like localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
        // Split each line into words
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
        // Count each word in each batch
        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

// Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print();
        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }
}