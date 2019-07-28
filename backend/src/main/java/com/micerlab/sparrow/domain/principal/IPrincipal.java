package com.micerlab.sparrow.domain.principal;

import java.util.List;

/**
 * 登录凭证接口
 */
public interface IPrincipal {

    /**
     * 判断访者是否是游客
     * @return 如果是返回 true
     */
    boolean isAnonymous();

    /**
     * 判断访者是否是用户
     * @return 如果是返回 true
     */
    boolean isGuest();

    /**
     * 返回用户id
     * @return 用户id
     */
    public String getUser_id();

    /**
     * 返回用户名
     * @return 用户名
     */
    public String getName();

    /**
     * 返回用户的邮箱
     * @return 用户邮箱
     */
    public String getEmail();

    /**
     * 返回用户工号
     * @return 用户工号
     */
    public String getWork_no();

    /**
     * 返回用户所属群组；游客则返回空链表
     * @return 群组唯一标识链表
     *
     */
    public List<String> getGroupsIdList();

}
