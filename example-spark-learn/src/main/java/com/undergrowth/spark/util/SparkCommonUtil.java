package com.undergrowth.spark.util;

import org.apache.spark.SparkConf;

/**
 * spark工具
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2017-10-31-10:59
 */
public class SparkCommonUtil {

  private static boolean isHome = false;
  private static final String homeSparkHome = "";
  private static final String companySparkHome = "E:\\java_other\\bigdata\\spark\\spark-2.2.0-bin-hadoop2.7";


  public static SparkConf getSparkConf(String appName, String master) {
    SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
    if (isHome) {
      conf.setSparkHome(homeSparkHome);
    } else {
      conf.setSparkHome(companySparkHome);
    }
    return conf;
  }

}