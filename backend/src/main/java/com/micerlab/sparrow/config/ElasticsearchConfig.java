package com.micerlab.sparrow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "elasticsearch-config")
public class ElasticsearchConfig
{
    private String host = "localhost";
    
    private int port = 9200;
    
    private final Indices indices = new Indices();
    
    @Getter
    @Setter
    public static class Indices
    {
        private String doc = "spa_docs";
        
        private String file = "spa_files";
        
        private String tag = "spa_tags";
        
        private String category = "spa_categories";
        
        private String user_group = "spa_groups";
        
    }
}
