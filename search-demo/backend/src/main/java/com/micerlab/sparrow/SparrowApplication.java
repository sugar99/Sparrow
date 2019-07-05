package com.micerlab.sparrow;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @see org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
 */
@SpringBootApplication
public class SparrowApplication
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value("${elasticsearch-config.host}")
    private String elasticsearchHost;
    
    @Value("${elasticsearch-config.port}")
    private int elasticsearchPort;

    @Bean
    public RestHighLevelClient elasticsearchClient()
    {
        logger.debug("elasticsearch host: " + elasticsearchHost + ";port: " + elasticsearchPort);
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticsearchHost, elasticsearchPort, "http")));
    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(SparrowApplication.class, args);
    }

}
