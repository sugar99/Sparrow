package com.micerlab.sparrow.service.user;

import com.micerlab.sparrow.domain.Result;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {

    public Result createUser(Map<String, Object> paramMap);

    public Result userLogin(HttpServletResponse response, Map<String, Object> paramMap);

    public Result getUserMeta(String user_id);

    public Result userLogout(String user_id);

    public Result getUserGroups(String user_id);
}
