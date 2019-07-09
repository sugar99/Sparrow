package com.micerlab.sparrow.dao.postgre;

import com.micerlab.sparrow.domain.pojo.Directory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DirectoryDao {

    void createDir(@Param("directory") Directory dir);

    Directory getDir(@Param("id") String id);

    void updateDir(@Param("id") String dir_id, @Param("title") String title);

    void deleteDir(@Param("id") String dir_id);

    void setMasterDir(@Param("master_id") String master_id, @Param("slave_id") String slave_id);

    Directory getMasterDir(@Param("slave_id") String slave_id);

    void dropSlaveDir(@Param("slave_id") String slave_id);

    List<Map<String, Object>> getOneLevelSlaves(@Param("master_id") String master_id);

    List<String> getTotalSlavesId(@Param("master_id") String master_id);

    List<Map<String, Object>> getRootPathDirs(@Param("slave_id") String slave_id);

    Directory getHomeDir();

    Directory getRootDir();
}
