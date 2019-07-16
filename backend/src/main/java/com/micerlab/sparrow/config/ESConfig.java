package com.micerlab.sparrow.config;

import com.micerlab.sparrow.dao.es.ESBaseDao;
import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import com.micerlab.sparrow.utils.BusinessException;
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
public class ESConfig
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
    
        public String spaFilterIndex(SpaFilterType spaFilterType)
        {
            if(spaFilterType == SpaFilterType.TAG)
                return getTag();
            else if(spaFilterType == SpaFilterType.CATEGORY)
                return getCategory();
            throw new BusinessException(ErrorCode.PARAM_ERR_FILTER_TYPE);
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
    
}
