package com.micerlab.sparrow.service.user;

import com.micerlab.sparrow.dao.postgre.ResourceDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.pojo.User;
import com.micerlab.sparrow.domain.principal.UserPrincipal;
import com.micerlab.sparrow.eventBus.event.user.InsertUserEvent;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.JwtUtil;
import com.micerlab.sparrow.utils.SpringContextUtil;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private ResourceDao resourceDao;

    private final long EXIPIRE_TIME = 60 * 1000 * 60 * 24;

    /**
     * 新建用户
     * @param paramMap 参数
     * @return Result (data: null)
     */
    @Override
    public Result createUser(Map<String, Object> paramMap) {
        //用户基本信息
        String user_id = UUID.randomUUID().toString();
        String username = paramMap.get("username").toString();
        String password = paramMap.get("password").toString();
        String work_no = paramMap.get("work_no").toString();
        String email = paramMap.get("email").toString();
        //工号唯一
        if (userDao.getUserMetaByWorkNo(work_no) != null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST_COMMON, "工号已经存在");
        }
        //产生InsertUserEvent，同步更新ES和PostgreSQL中的数据
        EventBus.getDefault().post(new InsertUserEvent(user_id, username, work_no, password, email));
        return Result.OK().build();
    }

    /**
     * 用户登录
     * @param response 请求响应
     * @param paramMap 参数
     * @return Result (data: userInfo, personal_dir, home, root)
     */
    @Override
    public Result userLogin(HttpServletResponse response, Map<String, Object> paramMap) {
        //表单
        String work_no = paramMap.get("work_no").toString();
        String password = paramMap.get("password").toString();

        User user = userDao.getUserMetaByWorkNo(work_no);
        if (user == null) {
            //用户不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_USERNAME_OR_PASSWORD_INVALID, "用户不存在");
        } else {
            if (!password.equals(user.getPassword())) {
                //密码错误
                throw new BusinessException(ErrorCode.NOT_FOUND_USERNAME_OR_PASSWORD_INVALID, "密码错误");
            } else {
                // 登录成功，将Token放入cookie中
                String user_id = user.getUser_id();
                String token = JwtUtil.createToken(user_id);
                Cookie cookie = new Cookie("auth_token", token);
                cookie.setPath("/");
                response.addCookie(cookie);
                // 将用户信息存入Redis中
                UserPrincipal userPrincipal = new UserPrincipal(user_id, user.getUsername(), user.getEmail(), work_no);
                List<String> userGroupsIdList = userDao.getUserGroupIds(user_id);
                userPrincipal.setGroupsIdList(userGroupsIdList);
                RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
                redisTemplate.opsForValue().set(user_id, userPrincipal);
                redisTemplate.expire(user_id, EXIPIRE_TIME, TimeUnit.MILLISECONDS);
                return Result.OK().data(defaultUserState(user_id)).build();
            }
        }
    }

    /**
     * 返回用户的元数据
     * @param user_id 用户id
     * @return Result (data: userInfo, personal_dir, home, root)
     */
    @Override
    public Result getUserMeta(String user_id) {
        return Result.OK().data(defaultUserState(user_id)).build();
    }

    /**
     * 登录注销
     * @param user_id 用户id
     * @return Result (data: null)
     */
    @Override
    public Result userLogout(String user_id) {
        //在redis中将用户信息删除
        RedisTemplate<Serializable, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate");
        redisTemplate.delete(user_id);
        return Result.OK().build();
    }

    /**
     * 获取用户当前所在群组
     * @param user_id 用户id
     * @return Result (data: groupsInfoList)
     */
    @Override
    public Result getUserGroups(String user_id) {
        return Result.OK().data(userDao.getUserGroups(user_id)).build();
    }

    /**
     * 用户登录和获取用户元数据时，返回用户个人信息，用户工作区信息给前端
     * @param user_id 用户id
     * @return Map {userInfo, personal_dir, master_dirs}
     */
    @Override
    public Map<String, Object> defaultUserState(String user_id) {
        //构造返回给前端的信息
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        List<Map<String, String>> master_dirs = new ArrayList<>();
        User user = userDao.getUserMetaById(user_id);
        //用户个人信息
        userInfo.put("work_no", user.getWork_no());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        //用户工作区
        String resource_id = user.getPersonal_dir();
        String resource_name = user.getUsername();
        List<String> resourceIdList = resourceDao.getRootPathResource(resource_id);
        //工作区目录的上层目录，用于前端目录跳转
        for (String master_id: resourceIdList) {
            Map<String, String> masterInfo = new HashMap<>();
            masterInfo.put("resource_id", master_id);
            masterInfo.put("resource_name", resourceDao.getResourceMeta(master_id).getResource_name());
            master_dirs.add(masterInfo);
        }
        resultMap.put("userInfo", userInfo);
        resultMap.put("resource_id", resource_id);
        resultMap.put("resource_name", resource_name);
        resultMap.put("master_dirs", master_dirs);
        return resultMap;
    }


}
