package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.config.ElasticsearchConfig;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ESDaoBean
{
    private Logger logger = LoggerFactory.getLogger(ESBaseDao.class);
    
    @Autowired
    private ElasticsearchConfig elasticsearchConfig;
    
    @Bean
    public RestHighLevelClient restHighLevelClient()
    {
        int port = elasticsearchConfig.getPort();
        String host = elasticsearchConfig.getHost();
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
        return new SpaDocDao(esBaseDao, elasticsearchConfig.getIndices());
    }
    
    @Bean
    public SpaFileDao spaFileDao()
    {
        return new SpaFileDao(esBaseDao, elasticsearchConfig.getIndices());
    }
}
