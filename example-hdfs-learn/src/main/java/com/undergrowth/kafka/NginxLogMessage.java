
package com.undergrowth.kafka;

import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class NginxLogMessage {

    @Expose
    private String agent;
    @Expose
    @SerializedName("app_name")
    private String appName;
    @Expose
    @SerializedName("cli_ip")
    private String cliIp;
    @Expose
    private String host;
    @Expose
    @SerializedName("http_host")
    private String httpHost;
    @Expose
    private String ref;
    @Expose
    private String req;
    @Expose
    @SerializedName("req_body")
    private String reqBody;
    @Expose
    private Map<String,String> reqBodyMap;
    @Expose
    @SerializedName("req_len")
    private String reqLen;
    @Expose
    @SerializedName("resp_time")
    private Double respTime;
    @Expose
    private Long size;
    @Expose
    private String status;
    @Expose
    private String stoken;
    @Expose
    @SerializedName("time_local")
    private String timeLocal;
    @Expose
    @SerializedName("timestamp_local")
    private String timestampLocal;
    @Expose
    @SerializedName("ups_host")
    private String upsHost;
    @Expose
    @SerializedName("ups_time")
    private String upsTime;
    @Expose
    private String url;
    @Expose
    private String xff;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCliIp() {
        return cliIp;
    }

    public void setCliIp(String cliIp) {
        this.cliIp = cliIp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHttpHost() {
        return httpHost;
    }

    public void setHttpHost(String httpHost) {
        this.httpHost = httpHost;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public String getReqLen() {
        return reqLen;
    }

    public void setReqLen(String reqLen) {
        this.reqLen = reqLen;
    }

    public Double getRespTime() {
        return respTime;
    }

    public void setRespTime(Double respTime) {
        this.respTime = respTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoken() {
        return stoken;
    }

    public void setStoken(String stoken) {
        this.stoken = stoken;
    }

    public String getTimeLocal() {
        return timeLocal;
    }

    public void setTimeLocal(String timeLocal) {
        this.timeLocal = timeLocal;
    }

    public String getTimestampLocal() {
        return timestampLocal;
    }

    public void setTimestampLocal(String timestampLocal) {
        this.timestampLocal = timestampLocal;
    }

    public String getUpsHost() {
        return upsHost;
    }

    public void setUpsHost(String upsHost) {
        this.upsHost = upsHost;
    }

    public String getUpsTime() {
        return upsTime;
    }

    public void setUpsTime(String upsTime) {
        this.upsTime = upsTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getXff() {
        return xff;
    }

    public void setXff(String xff) {
        this.xff = xff;
    }

    public Map<String, String> getReqBodyMap() {
        return reqBodyMap;
    }

    public void setReqBodyMap(Map<String, String> reqBodyMap) {
        this.reqBodyMap = reqBodyMap;
    }

    @Override
    public String toString() {
        return "NginxLogMessage{" +
            "agent='" + agent + '\'' +
            ", appName='" + appName + '\'' +
            ", cliIp='" + cliIp + '\'' +
            ", host='" + host + '\'' +
            ", httpHost='" + httpHost + '\'' +
            ", ref='" + ref + '\'' +
            ", req='" + req + '\'' +
            ", reqBody='" + reqBody + '\'' +
            ", reqBodyMap=" + reqBodyMap +
            ", reqLen='" + reqLen + '\'' +
            ", respTime=" + respTime +
            ", size=" + size +
            ", status='" + status + '\'' +
            ", stoken='" + stoken + '\'' +
            ", timeLocal='" + timeLocal + '\'' +
            ", timestampLocal='" + timestampLocal + '\'' +
            ", upsHost='" + upsHost + '\'' +
            ", upsTime='" + upsTime + '\'' +
            ", url='" + url + '\'' +
            ", xff='" + xff + '\'' +
            '}';
    }
}
