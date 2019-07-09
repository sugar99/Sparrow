package com.micerlab.sparrow.dao.postgre;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ACLDao {

    /**
     * 获取群组对指定资源的权限
     * @param group_id 群组id
     * @param resource_id 资源id
     * @return permission
     */
    String getGroupPermission(@Param("group_id") String group_id, @Param("resource_id") String resource_id);

    /**
     * 添加群组权限
     * @param group_id 群组id
     * @param resource_id 资源id
     * @param permission 权限
     */
    void addGroupPermission(@Param("group_id") String group_id, @Param("resource_id") String resource_id,
                            @Param("permission") String permission);

    /**
     * 更新群组对指定资源的权限
     * @param group_id 群组id
     * @param resource_id 资源id
     * @param permission 权限
     */
    void updateGroupPermission(@Param("group_id") String group_id, @Param("resource_id") String resource_id,
                               @Param("permission") String permission);

    /**
     * 删除群组对指定资源的权限
     * @param group_id 群组id
     * @param resource_id 资源id
     */
    void deleteGroupPermission(@Param("group_id") String group_id, @Param("resource_id") String resource_id);

    /**
     * 删除资源与所有群组的权限关系
     * @param resource_id 资源id
     */
    void deleteResourceAllPermission(@Param("resource_id") String resource_id);
}
