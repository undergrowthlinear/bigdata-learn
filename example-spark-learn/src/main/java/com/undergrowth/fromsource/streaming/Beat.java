
package com.undergrowth.fromsource.streaming;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Beat implements Serializable{

    @Expose
    private String hostname;
    @Expose
    private String name;
    @Expose
    private String version;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
