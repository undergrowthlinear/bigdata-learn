package com.undergrowth.fromsource.streaming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import spark.StreamingExamples;

/**
 * kafka流计数
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-18-15:25
 */
public class JavaKafkaStreamingCount {

    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws InterruptedException {

        StreamingExamples.setStreamingLogLevels();
        String zkHosts = "s3.ds.tc:2181,s4.ds.tc:2181,s5.ds.tc:2181";
        String groupId = "testHello";
        Map<String, Integer> topicToNum = new HashMap<>();
        topicToNum.put("HelloWorld", 2);
        //
        SparkConf sparkConf = new SparkConf().setAppName(JavaKafkaStreamingCount.class.getSimpleName()).setMaster("local[2]");
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, new Duration(2000));
        //
        JavaPairReceiverInputDStream<String, String> messageDStream = KafkaUtils.createStream(streamingContext, zkHosts, groupId, topicToNum);
        // Tuple2::_2
        JavaDStream<String> lines = messageDStream.map(tuple -> tuple._2);
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(SPACE.split(x)).iterator());
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1)).reduceByKey((i1, i2) -> i1 + i2);
        wordCounts.print();
        streamingContext.start();
        streamingContext.awaitTermination();
    }

}