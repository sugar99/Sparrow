package com.micerlab.sparrow.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Spring security 跨域请求路径匹配器
 * @author chenlvjia
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher
{
    // 允许任意接口使用以下HTTP方法
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    
    private PathMatcher matcher = new AntPathMatcher();
    
    private final Logger logger = LoggerFactory.getLogger(CsrfSecurityRequestMatcher.class);
    
    @Override
    public boolean matches(HttpServletRequest request)
    {
        // 允许post, put, delete等请求的url路径
        List<String> execludeUrls = new ArrayList<>();
        // 对v1, v2, v3, ... 的接口开放
        execludeUrls.add("/v{:\\d+}/**");
        
        if (execludeUrls.size() > 0)
        {
            String servletPath = request.getServletPath();
            for (String url : execludeUrls)
            {
                if (matcher.match(url, servletPath))
                    return false; // 允许对该url执行Post等方法的请求
            }
        }
        
        
        return !allowedMethods.matcher(request.getMethod()).matches();
    }
}
