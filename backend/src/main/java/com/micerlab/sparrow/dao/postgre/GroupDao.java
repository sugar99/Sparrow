package com.micerlab.sparrow.dao.postgre;

import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import com.micerlab.sparrow.domain.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface GroupDao {

    /**
     * 新建群组
     * @param group Group obj
     */
    void createGroup(@Param("group") Group group);

    /**
     * 获得群组元数据
     * @param group_id 群组id
     * @return Group obj
     */
    Group getGroupMeta(@Param("group_id") String group_id);

    String getGroupOwnerId(@Param("group_id") String group_id);

    void updateGroupMeta(@Param("group_id") String group_id, @Param("group_name") String group_name,
                         @Param("group_desc") String group_desc);

    /**
     * 删除群组
     * @param group_id 群组id
     */
    void deleteGroup(@Param("group_id") String group_id);

    /**
     * 添加成员
     * @param group_id 群组id
     * @param user_id 用户id
     * @param created_at 时间
     */
    void addMember(@Param("group_id") String group_id, @Param("user_id") String user_id, @Param("created_at") Timestamp created_at);

    /**
     * 获取所有成员信息
     * @param group_id 群组id
     * @return 用户列表
     */
    List<User> getGroupMembers(@Param("group_id") String group_id);

    /**
     * 移除成员
     * @param group_id 群组id
     * @param user_id 用户id
     */
    void removeMember(@Param("group_id") String group_id, @Param("user_id") String user_id);

    /**
     * 移除所有的成员（删除群组时的附加动作）
     * @param group_id 群组id
     */
    void removeAllMembers(@Param("group_id") String group_id);

    /**
     * 移除群组和资源的操作权限关系（删除群组时的附加动作）
     * @param group_id 群组id
     */
    void removeAllResource(@Param("group_id") String group_id);

    List<Map<String, String>> getGroupResources(@Param("group_id") String group_id);
}
