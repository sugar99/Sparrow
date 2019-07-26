package com.micerlab.sparrow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * @Description 为SwaggerUI添加账户、密码
 * @Author Honda, Lvjia
 * @Date 2019/7/18 15:45
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private SparrowConfig sparrowConfig;
    
    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        SparrowConfig.DeveloperAccount developerAccount = sparrowConfig.getDeveloperAccount();
        logger.info("developer account: username: " + developerAccount.getUsername() +
                "; password: " + developerAccount.getPassword());
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username(developerAccount.getUsername())
                        .password(developerAccount.getPassword())
                        .roles("USER")
                        .build();
        
        return new InMemoryUserDetailsManager(user);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // 配置1：暴力配置，只允许 v1
//        http
//                .authorizeRequests()
//                    .antMatchers("/v{\\d}/**").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                    .httpBasic()
//                .and()
//                    .logout()
//                    .permitAll();
    
        // 拦截swagger ui相关的url
        String[] swaggerURIPrefixs = {
                "/v2/api-docs**",
                "/v2/api-docs/**",
                "/webjars/springfox-swagger-ui**",
                "/webjars/springfox-swagger-ui/**",
                "/swagger-ui.html**",
                "/swagger-ui.html/**",
                "/swagger-resources**",
                "/swagger-resources/**",
        };
        
        http
                .authorizeRequests()
                .antMatchers(
                        swaggerURIPrefixs
                ).authenticated()
                .and()
                .httpBasic();
        
        // spring security csrf 防御默认只允许get,head,option等请求
        // 对v1, v2, v3, ... 的接口开放 post, put, delete 等操作
        RequestMatcher requestMatcher = new CsrfSecurityRequestMatcher();
        http.csrf().requireCsrfProtectionMatcher(requestMatcher);
    }
    
    
}
