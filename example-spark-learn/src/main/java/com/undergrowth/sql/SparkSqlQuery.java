package com.undergrowth.sql;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description sql查询
 * @date 2017-10-31-23:29
 */
public class SparkSqlQuery {

  public static void main(String[] args) throws AnalysisException {
    SparkSession spark = SparkSession
        .builder()
        .appName("Java Spark SQL basic.basic example")
        .config("spark.some.config.option", "some-value")
        .master("local")
        .getOrCreate();
    Dataset<Row> df = spark.read().json("example-spark-learn\\src\\main\\resources\\people.json");
    // Register the DataFrame as a SQL temporary view
    df.createOrReplaceTempView("people");
    Dataset<Row> sqlDF = spark.sql("SELECT * FROM people");
    sqlDF.show();
    // Register the DataFrame as a global temporary view
    df.createGlobalTempView("people");
    // Global temporary view is tied to a system preserved database `global_temp`
    spark.sql("SELECT * FROM global_temp.people").show();
    // Global temporary view is cross-session
    spark.newSession().sql("SELECT * FROM global_temp.people").show();
  }

}
