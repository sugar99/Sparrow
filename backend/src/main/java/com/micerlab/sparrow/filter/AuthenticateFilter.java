package com.micerlab.sparrow.filter;

import com.micerlab.sparrow.config.AccessManager;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.principal.UserPrincipal;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.JwtUtil;
import com.micerlab.sparrow.utils.SpringContextUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class AuthenticateFilter extends OncePerRequestFilter {

    private String user_id;
    
    private String test_id = "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
        logger.debug("访问：" + request.getMethod() + " " + request.getRequestURI());
        if (!AccessManager.mathAuthenticateUriList(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!isAuthenticatedUser(request)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_COMMON, "用户未登录");
        } else {
            //将用户信息存放到 request.attribute中，整个请求的上下文都可以使用用户信息
            RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
//            UserPrincipal userPrincipal = (UserPrincipal) redisTemplate.opsForValue().get(user_id);
            // TODO:
            UserPrincipal userPrincipal = (UserPrincipal) redisTemplate.opsForValue().get(test_id);
            request.setAttribute("principal", userPrincipal);
            filterChain.doFilter(request, response);
            return;
        }
    }

    /**
     * 判断是否为合法用户
     * @param request 请求
     * @return boolean
     */
    private boolean isAuthenticatedUser(HttpServletRequest request) {
//        String user_id = JwtUtil.getUser_id(request);
        //请求不带Token或Token不合法，返回false
//        if (user_id == null) {
//            return false;
//        }
//        this.user_id = user_id;
        //用户已注销，返回false
        String zhangSanId = "e1f5f562-2e96-4b3e-a6ff-e3f953c5b368";
        RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
        return redisTemplate.opsForValue().get(zhangSanId) != null;
    }
}
