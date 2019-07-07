package com.micerlab.sparrow.service.base;

import com.micerlab.sparrow.domain.principal.UserPrincipal;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Service("baseService")
public class BaseService {

    public static UserPrincipal getUserPrincipal(HttpServletRequest request) {
        return (UserPrincipal) request.getAttribute("principal");
    }

    /**
     * 获取用户群组列表
     * @return groupsIdList
     */
    public static List<String> getGroupIdList(HttpServletRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) request.getAttribute("principal");
        return userPrincipal.getGroupsIdList();
    }

    /**
     * 获取用户id
     * @return user_id
     */
    public static String getUser_Id(HttpServletRequest request) {
        UserPrincipal userPrincipal = (UserPrincipal) request.getAttribute("principal");
        return userPrincipal.getUser_id();
    }
}
