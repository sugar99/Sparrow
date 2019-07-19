package com.micerlab.sparrow.config;

import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.pojo.User;
import com.micerlab.sparrow.domain.principal.UserPrincipal;
import com.micerlab.sparrow.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class StartUpState implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartUpState.class);

    @Value("${sparrow.require-login:true}")
    private boolean login;

    private final long EXIPIRE_TIME = 60 * 1000 * 60 * 24 * 100;

    @Override
    public void run(String... args) throws Exception {
        //系统演示，默认admin账户登录
        if (!login) {
            logger.info("--------------系统演示，默认admin账户登录-----------");
            UserDao userDao = SpringContextUtil.getBean("userDao");
            User user = userDao.getUserMetaById(userDao.getAdminId());
            UserPrincipal userPrincipal = new UserPrincipal(user.getUser_id(), user.getUsername(),
                    user.getEmail(), user.getWork_no());
            List<String> userGroupsIdList = userDao.getUserGroupIds(user.getUser_id());
            userPrincipal.setGroupsIdList(userGroupsIdList);
            RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
            redisTemplate.opsForValue().set(user.getUser_id(), userPrincipal);
            redisTemplate.expire(user.getUser_id(), EXIPIRE_TIME, TimeUnit.MILLISECONDS);
        } else
            logger.info("-----------------------需要登录---------------------");
    }
}
