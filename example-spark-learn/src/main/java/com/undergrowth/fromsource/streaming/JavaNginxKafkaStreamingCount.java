package com.undergrowth.fromsource.streaming;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
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
public class JavaNginxKafkaStreamingCount {

    private static volatile Accumulator<Integer> countAllUvAccumulator = null;
    private static volatile Accumulator<Integer> countNewsUvAccumulator = null;
    private static volatile Broadcast<List<String>> broadcastUrl = null;

    private final static String param_sep = "&";
    private final static String key_val_sep = "=";

    private static Charset getDefaultCharset() {
        return Charset.forName("UTF-8");
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length != 5) {
            System.err.println("Usage: JavaNginxKafkaStreamingCount <bootstrap-servers> " +
                "<topics> <parti-num> <consumer-group-id> <master-url>");
            System.exit(1);
        }

        StreamingExamples.setStreamingLogLevels();
        String zkHosts = args[0];//"s3.ds.tc:2181,s4.ds.tc:2181,s5.ds.tc:2181";
        String topicName = args[1];//"nginx-to-kafka-to-hdfs";
        Integer numParti = Integer.valueOf(args[2]);//5;
        String groupId = args[3];//"nginx-consu-group";
        String masterUrl = args[4]; // "local[5]"

        Map<String, Integer> topicToNum = new HashMap<>();
        topicToNum.put(topicName, numParti);

        //
        SparkConf sparkConf = new SparkConf().setAppName(JavaNginxKafkaStreamingCount.class.getSimpleName()).setMaster(masterUrl);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, new Duration(2000));
        countAllUvAccumulator = streamingContext.sparkContext().accumulator(0, "countAllUvAccumulator");
        countNewsUvAccumulator = streamingContext.sparkContext().accumulator(0, "countNewsUvAccumulator");
        // 暂时只过滤资讯业务
        broadcastUrl = streamingContext.sparkContext().broadcast(Arrays.asList("/iyourcar_news_suv"));
        //
        JavaPairReceiverInputDStream<String, String> messageDStream = KafkaUtils.createStream(streamingContext, zkHosts, groupId, topicToNum);
        //
        JavaDStream<NginxLogBean> lines = messageDStream.map(
            (Function<Tuple2<String, String>, NginxLogBean>) v1 -> {
                NginxLogBean nginxLogBean = JSON.parseObject(v1._2, NginxLogBean.class);
                if (!StringUtils.isEmpty(nginxLogBean.getMessage())) {
                    nginxLogBean.setMessageBean(JSON.parseObject(nginxLogBean.getMessage(), NginxLogMessage.class));
                    if (!StringUtils.isEmpty(nginxLogBean.getMessageBean().getReqBody())) { // 对reqBody进行进一步解析
                        try {
                            String[] reqBodyParamArray = URLDecoder.decode(nginxLogBean.getMessageBean().getReqBody(), getDefaultCharset().name())
                                .split(param_sep);
                            Map<String, String> parKeyMap = Maps.newHashMap();
                            Arrays.stream(reqBodyParamArray).forEach(s -> parKeyMap.put(s.split(key_val_sep)[0], s.split(key_val_sep)[1]));
                            if (!(parKeyMap.size() == 0)) {
                                nginxLogBean.getMessageBean().setReqBodyMap(parKeyMap);
                            }
                        } catch (Exception e) { //解析reqBody异常 忽略 交由业务系统进行处理

                        }
                    }
                }
                return nginxLogBean;
            });
        lines.foreachRDD((VoidFunction<JavaRDD<NginxLogBean>>) nginxLogBeanJavaRDD -> {
            nginxLogBeanJavaRDD.filter((Function<NginxLogBean, Boolean>) v1 -> {
                countAllUvAccumulator.add(1);
                if (v1.getMessageBean() != null) {
                    for (String url :
                        broadcastUrl.value()) {
                        if (StringUtils.isNotEmpty(v1.getMessageBean().getUrl()) && v1.getMessageBean().getUrl().startsWith(url)) {
                            countNewsUvAccumulator.add(1);
                            return true;
                        }
                    }
                }
                return false;
            }).collect();

            System.out.println("广播器里面的值为:" + broadcastUrl.value());
            System.out.println("所有计数器的值为:" + countAllUvAccumulator.value());
            System.out.println("资讯计数器的值为:" + countNewsUvAccumulator.value());
        });
        //lines.count().print();
        streamingContext.start();
        streamingContext.awaitTermination();
    }

}