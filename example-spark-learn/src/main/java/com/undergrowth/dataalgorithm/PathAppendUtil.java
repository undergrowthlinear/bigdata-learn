package com.undergrowth.dataalgorithm;

import java.util.Calendar;
import java.util.Date;

/**
 * 路径转换
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-10-24-16:56
 */
public class PathAppendUtil {

    private static final Calendar cal = Calendar.getInstance();

    public static String pathAppend(String path, String className) {
        cal.setTime(new Date());
        String suffix = className + "_" + cal.get(Calendar.MINUTE) + "_" + cal.get(Calendar.SECOND) + "_" + Math.random();
        return path.endsWith("/") ? path + suffix : path + "/" + suffix;
    }

}