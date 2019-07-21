package com.micerlab.sparrow;

import com.micerlab.sparrow.utils.SerializeUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

    @Bean("redisTemplate")
    public RedisTemplate<Serializable, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Serializable, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new SerializeUtil());
        return template;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(SparrowApplication.class, args);
    }

}
