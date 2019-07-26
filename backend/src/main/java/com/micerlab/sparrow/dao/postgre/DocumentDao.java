package com.micerlab.sparrow.dao.postgre;

import com.micerlab.sparrow.domain.pojo.Directory;
import com.micerlab.sparrow.domain.pojo.Document;
import com.micerlab.sparrow.domain.pojo.Group;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DocumentDao {

    /**
     * 创建文档
     * @param document Document
     */
    void createDoc(@Param("document")Document document);

    /**
     * 获取文档元数据
     * @param id Document's Id
     * @return Document
     */
    Document getDoc(@Param("id") String id);

    /**
     * 更新文档元数据
     * @param id Document's Id
     * @param title Document's Title
     */
    void updateDoc(@Param("id") String id, @Param("title") String title);

    /**
     * 删除文档
     * @param id Document's Id
     */
    void deleteDoc(@Param("id") String id);

    /**
     * 设置父目录
     * @param master_id Master Directory's Id
     * @param slave_id Slave Document's Id
     */
    void setMasterDir(@Param("master_id") String master_id, @Param("slave_id") String slave_id);

    /**
     * 获取父目录元数据
     * @param slave_id Slave Document's Id
     * @return Directory
     */
    Directory getMasterDir(@Param("slave_id") String slave_id);

    /**
     * 删除父子资源关系
     * @param slave_id Slave Document's Id
     */
    void dropSlaveDoc(@Param("slave_id") String slave_id);

    /**
     * 获取全部上级目录
     * @param slave_id Slave Document's Id
     * @return List of MasterInfo
     */
    List<Map<String, Object>> getRootPathDirs(@Param("slave_id") String slave_id);

    /**
     * 获取对资源有操作权限的群组列表
     * @param resource_id Resource's Id
     * @return List of Groups
     */
    List<Group> getAuthGroups(@Param("resource_id") String resource_id);
}

