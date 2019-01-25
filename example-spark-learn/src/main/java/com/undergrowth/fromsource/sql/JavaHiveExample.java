package com.undergrowth.fromsource.sql;

import java.io.File;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * hive操作示例
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-10-11:49
 */
public class JavaHiveExample {

    public static void main(String[] args) {
        String wareHouseLocation = new File("spark-warehouse").getAbsolutePath();
        SparkSession sparkSession = SparkSession.builder().master("local").appName(JavaHiveExample.class.getSimpleName())
            .config("spark.sql.warehouse.dir", wareHouseLocation)
            .enableHiveSupport()
            .getOrCreate();
        //
        sparkSession.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive");
        sparkSession.sql("LOAD DATA LOCAL INPATH 'example-spark-learn/src/main/resources/kv1.txt' INTO TABLE src");
        // 查询
        sparkSession.sql("select * from src").show();
        //聚合
        sparkSession.sql("select count(1) from src").show();
        //
        Dataset<Row> strDs = sparkSession.sql("select * from src where key<10 order by key desc");
        Dataset<String> stringDs = strDs.map((MapFunction<Row, String>) row -> "key:" + row.get(0) + ",value:" + row.get(1), Encoders.STRING());
        stringDs.show();
        //
        sparkSession.stop();
    }

}