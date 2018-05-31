package com.undergrowth.fromsource.streaming;

import java.util.Arrays;
import java.util.List;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

/**
 * 实例：利用广播进行黑名单过滤！ 检查新的数据 根据是否在广播变量-黑名单内，从而实现过滤数据。
 */
public class BroadcastAccumulator {
    /**
     * 创建一个List的广播变量
     */
    private static volatile Broadcast<List<String>> broadcastList = null;
    /**
     * 计数器！
     */
    private static volatile Accumulator<Integer> accumulator = null;

    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[2]").
            setAppName("WordCountOnlineBroadcast");
        JavaStreamingContext jsc = new JavaStreamingContext(conf, Durations.seconds(5));

        /**
         * 注意：分发广播需要一个action操作触发。
         * 注意：广播的是Arrays的asList 而非对象的引用。广播Array数组的对象引用会出错。
         * 使用broadcast广播黑名单到每个Executor中！
         */
        broadcastList = jsc.sparkContext().broadcast(Arrays.asList("Hadoop", "Mahout", "Hive"));
        /**
         * 累加器作为全局计数器！用于统计在线过滤了多少个黑名单！
         * 在这里实例化。
         */
        accumulator = jsc.sparkContext().accumulator(0, "OnlineBlackListCounter");

        JavaReceiverInputDStream<String> lines = jsc.socketTextStream("127.0.0.1", 9999);

        /**
         * 这里省去flatmap因为名单是一个个的！
         */
        JavaPairDStream<String, Integer> pairs = lines.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) {
                return new Tuple2<String, Integer>(word, 1);
            }
        });
        JavaPairDStream<String, Integer> wordsCount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) {
                return v1 + v2;
            }
        });
        /**
         * Funtion里面 前几个参数是 入参。
         * 后面的出参。
         * 体现在call方法里面！
         *
         */
        wordsCount.foreachRDD(new VoidFunction<JavaPairRDD<String, Integer>>() {
            @Override
            public void call(JavaPairRDD<String, Integer> stringIntegerJavaPairRDD) throws Exception {
                stringIntegerJavaPairRDD.filter(new Function<Tuple2<String, Integer>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<String, Integer> wordPair) throws Exception {
                        if (broadcastList.value().contains(wordPair._1)) {
                            /**
                             * accumulator不仅仅用来计数。
                             * 可以同时写进数据库或者缓存中。
                             */
                            accumulator.add(wordPair._2);
                            return false;
                        } else {
                            return true;
                        }
                    }

                    ;
                    /**
                     * 广播和计数器的执行，需要进行一个action操作！
                     */
                }).collect();
                System.out.println("广播器里面的值" + broadcastList.value());
                System.out.println("计时器里面的值" + accumulator.value());
            }
        });

        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}