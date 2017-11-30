package com.undergrowth.sql;

import com.undergrowth.bean.Person;
import java.util.Arrays;
import java.util.Collections;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 数据集demo
 * @date 2017-10-31-23:36
 */
public class SparkDataSetDemo {

  public static void main(String[] args) {
    SparkSession spark = SparkSession
        .builder()
        .appName("Java Spark SQL basic example")
        .config("spark.some.config.option", "some-value")
        .master("local")
        .getOrCreate();
    // Create an instance of a Bean class
    Person person = new Person();
    person.setName("Andy");
    person.setAge(32);

// Encoders are created for Java beans
    Encoder<Person> personEncoder = Encoders.bean(Person.class);
    Dataset<Person> javaBeanDS = spark.createDataset(
        Collections.singletonList(person),
        personEncoder
    );
    javaBeanDS.show();
    // Encoders for most common types are provided in class Encoders
    Encoder<Integer> integerEncoder = Encoders.INT();
    Dataset<Integer> primitiveDS = spark.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
    Dataset<Integer> transformedDS = primitiveDS.map(
        (MapFunction<Integer, Integer>) value -> value + 1,
        integerEncoder);
    System.out.println(transformedDS.collect());
    ; // Returns [2, 3, 4]

// DataFrames can be converted to a Dataset by providing a class. Mapping based on name
    String path = "example-spark-learn\\src\\main\\resources\\people.json";
    Dataset<Person> peopleDS = spark.read().json(path).as(personEncoder);
    peopleDS.show();
  }

}
