package com.undergrowth.fromsource.sql;

import static org.apache.spark.sql.functions.col;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * sql例子
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-09-16:38
 */
public class JavaSqlExample {

    public static void main(String[] agrs) throws AnalysisException {
        SparkSession sparkSession = SparkSession.builder().appName(JavaSqlExample.class.getSimpleName()).master("local").getOrCreate();
        //
        //System.out.println(JavaSqlExample.class.getClassLoader().getResource("people.json").toString());
        basicSelFilGro(sparkSession);
        runDatasetCreationExample(sparkSession);
        //
        sparkSession.stop();
    }

    private static void basicSelFilGro(SparkSession sparkSession) throws AnalysisException {
        Dataset<Row> df = sparkSession.read().json(JavaSqlExample.class.getClassLoader().getResource("people.json").toString());
        df.show();
        df.printSchema();
        // 基本查询
        //df.select("name").show();
        df.select(col("name"), col("age").plus(1)).show();
        // 过滤分组
        df.filter(col("age").gt(10)).show();
        df.groupBy("age").count().show();
        // 创建临时视图
        df.createGlobalTempView("people");
        sparkSession.sql("select * from global_temp.people").show();
        // 全局的是跨越 session
        sparkSession.newSession().sql("select * from global_temp.people").show();
    }

    private static void runDatasetCreationExample(SparkSession spark) {
        // $example on:create_ds$
        // Create an instance of a Bean class
        Person person = new Person();
        person.setName("Andy");
        person.setAge(32);

        // Encoders are created for Java beans
        Encoder<Person> personEncoder = Encoders.bean(Person.class);
        Dataset<Person> javaBeanDS = spark.createDataset( // 通过隐式转换
            Collections.singletonList(person),
            personEncoder
        );
        javaBeanDS.show();
        // +---+----+
        // |age|name|
        // +---+----+
        // | 32|Andy|
        // +---+----+

        // Encoders for most common types are provided in class Encoders
        Encoder<Integer> integerEncoder = Encoders.INT();
        Dataset<Integer> primitiveDS = spark.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
        Dataset<Integer> transformedDS = primitiveDS.map(
            (MapFunction<Integer, Integer>) value -> value + 1,
            integerEncoder);
        transformedDS.collect(); // Returns [2, 3, 4]

        // DataFrames can be converted to a Dataset by providing a class. Mapping based on name
        String path = JavaSqlExample.class.getClassLoader().getResource("people.json").toString();
        Dataset<Person> peopleDS = spark.read().json(path).as(personEncoder);
        peopleDS.show();
        // +----+-------+
        // | age|   name|
        // +----+-------+
        // |null|Michael|
        // |  30|   Andy|
        // |  19| Justin|
        // +----+-------+
        // $example off:create_ds$
    }

    public static class Person implements Serializable {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

}