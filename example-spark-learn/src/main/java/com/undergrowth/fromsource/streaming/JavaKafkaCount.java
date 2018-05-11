package com.undergrowth.fromsource.streaming;

import java.util.Arrays;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

/**
 * kafka test
 *
 * * Example:
 *    `$ bin/run-example \
 *      sql.streaming.JavaKafkaCount host1:port1,host2:port2 \
 *      subscribe topic1,topic2`
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-11-15:39
 */
public class JavaKafkaCount {

    public static void main(String[] args) throws StreamingQueryException {
        /*if (args.length < 3) {
            System.err.println("Usage: JavaKafkaCount <bootstrap-servers> " +
                "<subscribe-type> <topics>");
            System.exit(1);
        }*/

        /*String bootstrapServers = args[0];
        String subscribeType = args[1];
        String topics = args[2];*/
        String bootstrapServers = "s3.ds.tc:9092";
        String subscribeType = "subscribe";
        String topics = "HelloWorld";

        SparkSession spark = SparkSession
            .builder()
            .appName("JavaKafkaCount").master("local")
            .getOrCreate();

        // Create DataSet representing the stream of input lines from kafka
        Dataset<String> lines = spark
            .readStream()
            .format("kafka")
            .option("kafka.bootstrap.servers", bootstrapServers)
            .option(subscribeType, topics)
            .load()
            .selectExpr("CAST(value AS STRING)")
            .as(Encoders.STRING());

        // Generate running word count
        Dataset<Row> wordCounts = lines.flatMap(
            (FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(),
            Encoders.STRING()).groupBy("value").count();

        // Start running the query that prints the running counts to the console
        StreamingQuery query = wordCounts.writeStream()
            .outputMode("complete")
            .format("console")
            .start();

        query.awaitTermination();
    }

}