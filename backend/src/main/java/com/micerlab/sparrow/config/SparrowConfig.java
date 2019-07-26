package com.micerlab.sparrow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("sparrowConfig")
@ConfigurationProperties(prefix = "sparrow")
public class SparrowConfig {

    private String host;

    private boolean requirelogin;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean getRequirelogin() {
        return requirelogin;
    }

    public void setRequirelogin(boolean requirelogin) {
        this.requirelogin = requirelogin;
    }
}
