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

    private RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");

    private String user_id;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!isAuthenticatedUser(httpServletRequest)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_COMMON, "用户未登录");
        } else {
            UserPrincipal userPrincipal = (UserPrincipal) redisTemplate.opsForValue().get(user_id);
            httpServletRequest.setAttribute("principal", userPrincipal);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private boolean isAuthenticatedUser(HttpServletRequest request) {
        String user_id = JwtUtil.getUser_id(request);
        if (user_id == null) {
            return false;
        }
        this.user_id = user_id;
        return redisTemplate.opsForValue().get(user_id) != null;
    }
}
