package com.micerlab.sparrow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("sparrowConfig")
@ConfigurationProperties(prefix = "sparrow")
public class SparrowConfig {

    private final DeveloperAccount developerAccount = new DeveloperAccount();
    
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
    
    public DeveloperAccount getDeveloperAccount()
    {
        return developerAccount;
    }
    
    public static class DeveloperAccount
    {
        private String username;
        private String password;
    
        public String getUsername()
        {
            return username;
        }
    
        public void setUsername(String username)
        {
            this.username = username;
        }
    
        public String getPassword()
        {
            return password;
        }
    
        public void setPassword(String password)
        {
            this.password = password;
        }
    }
}
