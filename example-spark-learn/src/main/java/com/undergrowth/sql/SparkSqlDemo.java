package com.undergrowth.sql;

import static org.apache.spark.SparkContext.getOrCreate;
import static org.apache.spark.sql.functions.col;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description spark-sql
 * @date 2017-10-31-22:43
 */
public class SparkSqlDemo {

  public static void main(String[] args) {
    SparkSession spark = SparkSession
        .builder()
        .appName("Java Spark SQL basic example")
        .config("spark.some.config.option", "some-value")
        .master("local")
        .getOrCreate();
    Dataset<Row> df = spark.read().json("example-spark-learn\\src\\main\\resources\\people.json");

// Displays the content of the DataFrame to stdout
    df.show();
    // Print the schema in a tree format
    df.printSchema();
    // Select only the "name" column
    df.select("name").show();
    df.select(col("name"), col("age").plus(1)).show();
    // Select people older than 21
    df.filter(col("age").gt(21)).show();
    // Count people by age
    df.groupBy("age").count().show();
  }

}
