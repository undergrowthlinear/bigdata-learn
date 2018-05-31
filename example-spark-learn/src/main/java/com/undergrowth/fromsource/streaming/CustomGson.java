package com.undergrowth.fromsource.streaming;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;

/**
 * 自定义Gson
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-30-11:12
 */
public class CustomGson implements Serializable{

    private final String defaultDateFormatPattern = "yyyy-MM-dd'T'HH:mm:ssZ";

    private transient  Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(defaultDateFormatPattern).create();

    public Gson getGson() {
        return gson;
    }
}