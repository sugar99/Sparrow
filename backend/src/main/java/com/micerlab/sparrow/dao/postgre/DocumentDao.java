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

    void createDoc(@Param("document")Document document);

    Document getDoc(@Param("id") String id);

    void updateDoc(@Param("id") String id, @Param("title") String title);

    void deleteDoc(@Param("id") String id);

    void setMasterDir(@Param("master_id") String master_id, @Param("slave_id") String slave_id);

    Directory getMasterDir(@Param("slave_id") String slave_id);

    void dropSlaveDoc(@Param("slave_id") String slave_id);

    List<Map<String, Object>> getRootPathDirs(@Param("slave_id") String slave_id);

    List<Group> getAuthGroups(@Param("resource_id") String resource_id);
}

