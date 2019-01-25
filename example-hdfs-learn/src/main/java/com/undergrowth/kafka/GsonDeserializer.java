package com.undergrowth.kafka;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.ExtendedDeserializer;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * gson解析器
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-29-17:31
 */
public class GsonDeserializer<T> implements ExtendedDeserializer<T> {

    private final String defaultDateFormatPattern = "yyyy-MM-dd'T'HH:mm:ssZ";

    private final String param_sep = "&";
    private final String key_val_sep = "=";

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(defaultDateFormatPattern).create();

    private Class<T> targetType;

    public GsonDeserializer(Class<T> targetType, Gson gson) {
        Assert.notNull(targetType, "'targetType' must not be null.");
        this.targetType = targetType;
        if (gson != null) {
            this.gson = gson;
        }
    }

    public GsonDeserializer() {
        this((Class<T>) null, null);
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            T t = gson.fromJson(new StringReader(new String(data, getDefaultCharset())), targetType);
            if (t instanceof NginxLogBean) { // 对于nginx日志 做特殊处理 采用logstash使nginx的message为string ,可使用flume+kafka 或者作如下兼容
                NginxLogBean nginxLogBean = (NginxLogBean) t;
                if (!StringUtils.isEmpty(nginxLogBean.getMessage())) {
                    nginxLogBean.setMessageBean(gson.fromJson(nginxLogBean.getMessage(), NginxLogMessage.class));
                    if (!StringUtils.isEmpty(nginxLogBean.getMessageBean().getReqBody())) { // 对reqBody进行进一步解析
                        try {
                            String[] reqBodyParamArray = URLDecoder.decode(nginxLogBean.getMessageBean().getReqBody(), getDefaultCharset().name())
                                .split(param_sep);
                            Map<String, String> parKeyMap = Maps.newHashMap();
                            Arrays.stream(reqBodyParamArray).forEach(s -> parKeyMap.put(s.split(key_val_sep)[0], s.split(key_val_sep)[1]));
                            if (!CollectionUtils.isEmpty(parKeyMap)) {
                                nginxLogBean.getMessageBean().setReqBodyMap(parKeyMap);
                            }
                        } catch (Exception e) { //解析reqBody异常 忽略 交由业务系统进行处理

                        }
                    }
                }
            }
            return t;
        } catch (Exception e) {
            throw new SerializationException("Can't deserialize data [" + Arrays.toString(data) +
                "] from topic [" + topic + "]", e);
        }

    }

    private Charset getDefaultCharset() {
        return Charset.forName("UTF-8");
    }

    @Override
    public void close() {

    }

    public Gson getGson() {
        return gson;
    }
}