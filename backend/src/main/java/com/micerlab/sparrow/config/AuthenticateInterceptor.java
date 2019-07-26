package com.micerlab.sparrow.config;

import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.principal.UserPrincipal;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.JwtUtil;
import com.micerlab.sparrow.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class AuthenticateInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateInterceptor.class);

    private String user_id;

    @Value("${sparrow.require-login")
    private boolean require_login;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
        logger.debug("访问：" + request.getMethod() + " " + request.getRequestURI());
        if (request.getMethod().equals("OPTIONS")){
            return true;
        }
        if (!AccessManager.mathAuthenticateUriList(request.getRequestURI())) {
            logger.debug("访问的地址不需要认证");
            return true;
        }
        SparrowConfig sparrowConfig = SpringContextUtil.getBean("sparrowConfig");
        require_login = sparrowConfig.getRequirelogin();
        if (!isAuthenticatedUser(request)) {
            logger.debug("用户未登录");
            throw new BusinessException(ErrorCode.FORBIDDEN_COMMON, "用户未登录");
        } else {
            //将用户信息存放到 request.attribute中，整个请求的上下文都可以使用用户信息
            logger.debug("用户已登录");
            RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
            UserPrincipal userPrincipal = (UserPrincipal) redisTemplate.opsForValue().get(user_id);
            request.setAttribute("principal", userPrincipal);
            return true;
        }
    }
    /**
     * 判断是否为合法用户
     * @param request 请求
     * @return boolean
     */
    private boolean isAuthenticatedUser(HttpServletRequest request) {
        if (!require_login) {
            UserDao userDao = SpringContextUtil.getBean("userDao");
            this.user_id = userDao.getAdminId();
            return true;
        }
        String user_id = JwtUtil.getUser_id(request);
//        请求不带Token或Token不合法，返回false
        if (user_id == null) {
            return false;
        }
        this.user_id = user_id;
//        用户已注销，返回false
        RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
        return redisTemplate.opsForValue().get(user_id) != null;
    }
}
