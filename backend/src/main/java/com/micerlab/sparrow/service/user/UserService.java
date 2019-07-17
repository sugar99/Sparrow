package com.micerlab.sparrow.service.user;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.UserLoginParams;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService {

    Result createUser(Map<String, Object> paramMap);

    Result userLogin(HttpServletResponse response, UserLoginParams params);

    Result getUserMeta(String user_id);

    Result userLogout(String user_id);

    Result getUserGroups(String user_id);

    Map<String, Object> defaultUserState(String user_id);

    public List<String> getUserGroupsIdList(String user_id);
}
