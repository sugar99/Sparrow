package com.micerlab.sparrow.dao.postgre;

import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ResourceDao {

    /**
     * 创建资源
     * @param resource resource obj
     */
    void createResource(@Param("resource") Resource resource);

    /**
     * 获取资源元数据
     * @param resource_id 资源id
     * @return resource obj
     */
    Resource getResourceMeta(@Param("resource_id") String resource_id);

    /**
     * 更新资源meta
     * @param resource_id 资源id
     * @param resource_name 资源名称
     */
    void updateResourceMeta(@Param("resource_id") String resource_id, @Param("resource_name") String resource_name);

    /**
     * 删除资源
     * @param resource_id 资源id
     */
    void deleteResource(@Param("resource_id") String resource_id);

    /**
     * 创建上下级目录（目录文档）关系
     * @param master_id 父目录id
     * @param slave_id 子目录 or 文档 id
     */
    void createMasterSlaveRelation(@Param("master_id") String master_id, @Param("slave_id") String slave_id);

    /**
     * 删除上下级目录（目录文档）关系
     * @param slave_id 子目录 or 文档 id
     */
    void deleteMasterSlaveRelation(@Param("slave_id") String slave_id);

    /**
     * 获取子资源
     * @param master_id 父目录id
     * @return 资源列表
     */
    List<Map<String, Object>> getSlaveResources(@Param("master_id") String master_id);

    /**
     * 获取资源的全部子资源，子子资源...
     * @param master_id 父目录id
     * @return 资源id
     */
    List<String> getTotalSlaveResourcesId(@Param("master_id") String master_id);

    /**
     * 获取父辈，祖父辈...资源
     * @param slave_id 子目录 or 文档id
     * @return 资源id列表
     */
    List<String> getRootPathResource(@Param("slave_id") String slave_id);

    /**
     * 获取对该资源有权限的所有群组
     * @param resource_id 资源id
     * @return 群组列表
     */
    List<Group> getResourceGroups(@Param("resource_id") String resource_id);

    /**
     * 获取父辈资源元数据
     * @param slave_id 子资源id
     * @return Resource obj
     */
    Resource getMasterResourceMeta(@Param("slave_id") String slave_id);

    /**
     * 获取home目录id
     * @return home目录id
     */
    String getHomeDirId();


}
