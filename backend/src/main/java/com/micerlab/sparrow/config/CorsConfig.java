package com.micerlab.sparrow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
//@Component
//@Configuration
public class CorsConfig
{
    /**
     * 添加跨域配置，这通配符的配置有点简单粗暴...
     * @return 跨域配置过滤器
     */
//    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        //        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedOrigin("localhost:8080");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}

