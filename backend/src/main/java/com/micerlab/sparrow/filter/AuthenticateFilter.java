package com.micerlab.sparrow.filter;

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

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!isAuthenticatedUser(httpServletRequest)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_COMMON, "用户未登录");
        } else {
            //将用户信息存放到 request.attribute中，整个请求的上下文都可以使用用户信息
            RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
            UserPrincipal userPrincipal = (UserPrincipal) redisTemplate.opsForValue().get(user_id);
            httpServletRequest.setAttribute("principal", userPrincipal);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    /**
     * 判断是否为合法用户
     * @param request 请求
     * @return boolean
     */
    private boolean isAuthenticatedUser(HttpServletRequest request) {
        String user_id = JwtUtil.getUser_id(request);
        //请求不带Token或Token不合法，返回false
        if (user_id == null) {
            return false;
        }
        this.user_id = user_id;
        //用户已注销，返回false
        RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
        return redisTemplate.opsForValue().get(user_id) != null;
    }
}
