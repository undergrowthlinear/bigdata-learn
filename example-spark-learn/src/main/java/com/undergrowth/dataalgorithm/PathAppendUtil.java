package com.undergrowth.dataalgorithm;

/**
 * 路径转换
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-10-24-16:56
 */
public class PathAppendUtil {

    public static String pathAppend(String path, String className) {
        return path.endsWith("/") ? path + className + Math.random() : path + "/" + className + Math.random();
    }

}