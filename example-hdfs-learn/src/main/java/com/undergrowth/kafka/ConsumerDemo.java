package com.undergrowth.kafka;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * test
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-04-19-18:25
 */
public class ConsumerDemo {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "s3.ds.tc:9092");
        properties.put("group.id", "nginx-consu-group");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        //properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // properties.put("value.deserializer", "org.springframework.kafka.support.serializer.JsonDeserializer");
        GsonDeserializer gsonDeserializer = new GsonDeserializer<NginxLogBean>(NginxLogBean.class, null);
        KafkaConsumer<String, NginxLogBean> kafkaConsumer = new KafkaConsumer<>(properties, new StringDeserializer(),
            gsonDeserializer);

        // kafkaConsumer.subscribe(Arrays.asList("HelloWorld"));
        kafkaConsumer.subscribe(Arrays.asList("nginx-to-kafka-to-hdfs"));
        while (true) {
            ConsumerRecords<String, NginxLogBean> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, NginxLogBean> record : records) {
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }

    }
}