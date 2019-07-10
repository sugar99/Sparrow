package com.micerlab.sparrow;

import com.micerlab.sparrow.filter.AuthenticateFilter;
import com.micerlab.sparrow.utils.SerializeUtil;
import org.apache.http.HttpHost;
import org.apache.ibatis.annotations.Mapper;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @see org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
 */
@SpringBootApplication(scanBasePackages = "com.micerlab.sparrow")
@MapperScan(basePackages = "com.micerlab.sparrow.dao", annotationClass = Repository.class)
public class SparrowApplication
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
//    @Value("${elasticsearch-config.host}")
    @Value("localhost")
    private String elasticsearchHost;
    
//    @Value("${elasticsearch-config.port}")
    @Value("9200")
    private int elasticsearchPort;

    @Bean
    public RestHighLevelClient restHighLevelClient()
    {
        logger.debug("elasticsearch host: " + elasticsearchHost + ";port: " + elasticsearchPort);
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticsearchHost, elasticsearchPort, "http")));
    }

    @Bean("redisTemplate")
    public RedisTemplate<Serializable, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Serializable, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new SerializeUtil());
        return template;
    }


    @Bean
    public FilterRegistrationBean authenticateFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AuthenticateFilter());
        registrationBean.addUrlPatterns("/*");
//        registrationBean.addUrlPatterns("/v1/groups/*");
//        registrationBean.addUrlPatterns("/v1/users/*");
//        registrationBean.addUrlPatterns("/v1/resources/*");
//        registrationBean.addUrlPatterns("/v1/dirs/*");
//        registrationBean.addUrlPatterns("/v1/docs/*");
//        registrationBean.addUrlPatterns("/v1/files/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }


    public static void main(String[] args)
    {
        SpringApplication.run(SparrowApplication.class, args);
    }

}
