package com.undergrowth.kafka;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class NginxLogBean {

    @Expose
    private Beat beat;
    @Expose
    private String host;
    @Expose
    @SerializedName("input_type")
    private String inputType;
    @Expose
    private String message;
    @Expose
    private NginxLogMessage messageBean;
    @Expose
    private Long offset;
    @Expose
    private String source;
    @Expose
    private List<String> tags;
    @Expose
    @SerializedName("@timestamp")
    private String timestamp;
    @Expose
    private String type;
    @Expose
    @SerializedName("@version")
    private String version;

    public Beat getBeat() {
        return beat;
    }

    public void setBeat(Beat beat) {
        this.beat = beat;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public NginxLogMessage getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(NginxLogMessage messageBean) {
        this.messageBean = messageBean;
    }

    @Override
    public String toString() {
        return "NginxLogBean{" +
            "beat=" + beat +
            ", host='" + host + '\'' +
            ", inputType='" + inputType + '\'' +
            ", messageBean=" + messageBean +
            ", offset=" + offset +
            ", source='" + source + '\'' +
            ", tags=" + tags +
            ", timestamp='" + timestamp + '\'' +
            ", type='" + type + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
