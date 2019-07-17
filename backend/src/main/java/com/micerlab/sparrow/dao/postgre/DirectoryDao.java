package com.micerlab.sparrow.dao.postgre;

import com.micerlab.sparrow.domain.pojo.Directory;
import com.micerlab.sparrow.domain.pojo.Group;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DirectoryDao {
    /**
     * 创建目录
     * @param dir Directory
     */
    void createDir(@Param("directory") Directory dir);

    /**
     * 获取目录元数据
     * @param id Directory's Id
     * @return Directory
     */
    Directory getDir(@Param("id") String id);

    /**
     * 更新目录元数据
     * @param dir_id Directory's Id
     * @param title Directory's Title
     */
    void updateDir(@Param("id") String dir_id, @Param("title") String title);

    /**
     * 删除目录
     * @param dir_id Directory's Id
     */
    void deleteDir(@Param("id") String dir_id);

    /**
     * 设置父目录
     * @param master_id Master Directory's Id
     * @param slave_id Slave Directory's Id
     */
    void setMasterDir(@Param("master_id") String master_id, @Param("slave_id") String slave_id);

    /**
     * 获得父目录元数据
     * @param slave_id Slave Directory's Id
     * @return Directory
     */
    Directory getMasterDir(@Param("slave_id") String slave_id);

    /**
     * 删除父子目录关系
     * @param slave_id Slave Directory's Id
     */
    void dropSlaveDir(@Param("slave_id") String slave_id);

    /**
     * 获得目录的下级子资源
     * @param master_id Master Directory's Id
     * @return List of SlavesInfo
     */
    List<Map<String, Object>> getOneLevelSlaves(@Param("master_id") String master_id);

    /**
     * 获得目录的子资源id列表
     * @param master_id Master Directory's Id
     * @return List of SlavesId
     */
    List<String> getTotalSlavesId(@Param("master_id") String master_id);

    /**
     * 获取全部上级目录
     * @param slave_id Slaves Directory's Id
     * @return List of MasterInfo
     */
    List<Map<String, Object>> getRootPathDirs(@Param("slave_id") String slave_id);

    /**
     * 获取home目录元数据
     * @return Directory
     */
    Directory getHomeDir();

    /**
     * 获取root目录元数据
     * @return Directory
     */
    Directory getRootDir();

    int isModifiable(@Param("id") String id);

    /**
     * 获取对资源有操作权限的群组列表
     * @param resource_id Resource's Id
     * @return List of Groups
     */
    List<Group> getAuthGroups(@Param("resource_id") String resource_id);
}
