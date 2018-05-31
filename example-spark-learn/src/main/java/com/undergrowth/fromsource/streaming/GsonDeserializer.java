package com.undergrowth.fromsource.streaming;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.errors.SerializationException;

/**
 * gson解析器
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-29-17:31
 */
public class GsonDeserializer<T> implements Serializable {


    private final String param_sep = "&";
    private final String key_val_sep = "=";

    private CustomGson customGson = new CustomGson();

    private Class<T> targetType;

    public GsonDeserializer(Class<T> targetType, CustomGson customGson) {
        this.targetType = targetType;
        if (customGson != null) {
            this.customGson = customGson;
        }
    }

    public GsonDeserializer() {
        this((Class<T>) null, null);
    }

    public T deserializeStr(String data) {
        return deserialize(data.getBytes(getDefaultCharset()));
    }

    public T deserialize(byte[] data) {
        try {
            T t = customGson.getGson().fromJson(new StringReader(new String(data, getDefaultCharset())), targetType);
            if (t instanceof NginxLogBean) { // 对于nginx日志 做特殊处理 采用logstash使nginx的message为string ,可使用flume+kafka 或者作如下兼容
                NginxLogBean nginxLogBean = (NginxLogBean) t;
                if (!StringUtils.isEmpty(nginxLogBean.getMessage())) {
                    nginxLogBean.setMessageBean(customGson.getGson().fromJson(nginxLogBean.getMessage(), NginxLogMessage.class));
                    if (!StringUtils.isEmpty(nginxLogBean.getMessageBean().getReqBody())) { // 对reqBody进行进一步解析
                        try {
                            String[] reqBodyParamArray = URLDecoder.decode(nginxLogBean.getMessageBean().getReqBody(), getDefaultCharset().name())
                                .split(param_sep);
                            Map<String, String> parKeyMap = Maps.newHashMap();
                            Arrays.stream(reqBodyParamArray).forEach(s -> parKeyMap.put(s.split(key_val_sep)[0], s.split(key_val_sep)[1]));
                            if (!(parKeyMap.size() == 0)) {
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
                "] ", e);
        }

    }

    private Charset getDefaultCharset() {
        return Charset.forName("UTF-8");
    }


}