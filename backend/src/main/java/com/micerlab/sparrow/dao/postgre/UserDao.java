package com.micerlab.sparrow.dao.postgre;

import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDao")
public interface UserDao {

    /**
     * 新建用户
     * @param user User obj
     */
    void createUser(@Param("user") User user);

    /**
     * 通过用户id获取用户meta
     * @param user_id 用户id
     * @return User obj
     */
    User getUserMetaById(@Param("user_id") String user_id);

    /**
     * 通过用户工号获取用户meta
     * @param work_no 工号
     * @return User obj
     */
    User getUserMetaByWorkNo(@Param("work_no") String work_no);

    /**
     * 删除用户
     * @param user_id 用户id
     */
    void deleteUser(@Param("user_id") String user_id);

    /**
     * 获取用户所在群组的id
     * @param user_id 用户id
     * @return 群组id列表
     */
    List<String> getUserGroupIds(@Param("user_id") String user_id);

    /**
     * 获取用户所在的群组
     * @param user_id 用户id
     * @return 群组列表
     */
    List<Group> getUserGroups(@Param("user_id") String user_id);

    /**
     * 获取管理员id
     * @return user_id
     */
    String getAdminId();
}
