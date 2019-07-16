package com.micerlab.sparrow.config;

import com.micerlab.sparrow.dao.es.ESBaseDao;
import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elasticsearch-config")
public class ElasticsearchConfig
{
    public static class Indices
    {
        private String doc = "spa_docs";
        
        private String file = "spa_files";
        
        private String tag = "spa_tags";
        
        private String category = "spa_categories";
        
        private String user_group = "spa_groups";
        
        public String getDoc()
        {
            return doc;
        }
        
        public void setDoc(String doc)
        {
            this.doc = doc;
        }
        
        public String getFile()
        {
            return file;
        }
        
        public void setFile(String file)
        {
            this.file = file;
        }
        
        public String getTag()
        {
            return tag;
        }
        
        public void setTag(String tag)
        {
            this.tag = tag;
        }
        
        public String getCategory()
        {
            return category;
        }
        
        public void setCategory(String category)
        {
            this.category = category;
        }
        
        public String getUser_group()
        {
            return user_group;
        }
        
        public void setUser_group(String user_group)
        {
            this.user_group = user_group;
        }
    }
    
    private String host = "localhost";
    
    private int port = 9200;
    
    private final Indices indices = new Indices();
    
    public String getHost()
    {
        return host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public Indices getIndices()
    {
        return indices;
    }
    
    private Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);
    
    @Bean
    public RestHighLevelClient restHighLevelClient()
    {
        logger.debug("elasticsearch host: " + host + ";port: " + port);
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, port, "http")));
    }
    
    @Autowired
    private ESBaseDao esBaseDao;
    
    @Bean
    public SpaDocDao spaDocDao()
    {
        return new SpaDocDao(esBaseDao, indices);
    }
    
    @Bean
    public SpaFileDao spaFileDao()
    {
        return new SpaFileDao(esBaseDao, indices);
    }
}
