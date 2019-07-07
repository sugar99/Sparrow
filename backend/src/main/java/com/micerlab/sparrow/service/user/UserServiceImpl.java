package com.micerlab.sparrow.service.user;

import com.micerlab.sparrow.domain.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Override
    public Result createUser(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result userLogin(HttpServletResponse response, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result getUserMeta(String user_id) {
        return null;
    }

    @Override
    public Result userLogout(String user_id) {
        return null;
    }

    @Override
    public Result getUserGroups(String user_id) {
        return null;
    }
}
