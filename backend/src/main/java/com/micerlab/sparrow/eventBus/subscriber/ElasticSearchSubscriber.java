package com.micerlab.sparrow.eventBus.subscriber;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.dao.es.CUDUserGroupDao;
import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.doc.SpaDoc;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.eventBus.event.doc.DeleteDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.InsertDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.UpdateDocEvent;
import com.micerlab.sparrow.eventBus.event.file.UpdateFileThumbnailEvent;
import com.micerlab.sparrow.eventBus.event.group.InsertGroupEvent;
import com.micerlab.sparrow.eventBus.event.group.UpdateGroupEvent;
import com.micerlab.sparrow.eventBus.event.user.InsertUserEvent;
import com.micerlab.sparrow.utils.TimeUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Elasticsearch 事件订阅者
 */
@Component("elasticSearchSubscriber")
public class ElasticSearchSubscriber {

    @Autowired
    private CUDUserGroupDao cudUserGroupDao;

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SpaDocDao spaDocDao;
    
    @Autowired
    private SpaFileDao spaFileDao;

    public ElasticSearchSubscriber() {
        EventBus.getDefault().register(this);
    }

    /**
     * 新建用户
     * @param insertUserEvent 新建用户事件
     */
    @Subscribe
    public void insertUser(InsertUserEvent insertUserEvent) {
        String user_id = insertUserEvent.getUser_id();
        String username = insertUserEvent.getUsername();
        String work_no = insertUserEvent.getWork_no();
        cudUserGroupDao.insertUser(user_id, username, work_no);
    }

    /**
     * 新建群组
     * @param insertGroupEvent 新建群组事件
     */
    @Subscribe
    public void insertGroup(InsertGroupEvent insertGroupEvent) {
        Group group = insertGroupEvent.getGroup();
        String group_id = group.getGroup_id();
        String group_name = group.getGroup_name();
        String group_desc = group.getGroup_desc();
        String creator_id = group.getCreator_id();
        String creator = userDao.getUserMetaById(creator_id).getUsername();
        cudUserGroupDao.insertGroup(group_id, group_name, group_desc, creator);
    }

    /**
     * 更新群组元数据
     * @param updateGroupEvent 更新群组事件
     */
    @Subscribe
    public void updateGroup(UpdateGroupEvent updateGroupEvent) {
        String group_id = updateGroupEvent.getGroup_id();
        String group_name = updateGroupEvent.getGroup_name();
        String group_desc = updateGroupEvent.getGroup_desc();
        cudUserGroupDao.updateGroup(group_id, group_name, group_desc);
    }
    
    @Subscribe
    public void createDocMeta(InsertDocEvent insertDocEvent)
    {
        SpaDoc spaDoc = new SpaDoc(
                insertDocEvent.getResource_id(),
                insertDocEvent.getTitle(),
                insertDocEvent.getDesc(),
                insertDocEvent.getCreator(),
                Collections.emptyList(),
                TimeUtil.formatTimeStr(insertDocEvent.getCreated_time()),
                TimeUtil.formatTimeStr(insertDocEvent.getModified_time()),
                insertDocEvent.getMeta_state()
        );
        spaDocDao.index(spaDoc.getId(), spaDoc, true);
    }
    
    @Subscribe
    public void updateDocMeta(UpdateDocEvent updateDocEvent)
    {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("title", updateDocEvent.getTitle());
        jsonMap.put("desc", updateDocEvent.getDesc());
        jsonMap.put("modified_time", TimeUtil.formatTimeStr(updateDocEvent.getModified_time()));
        jsonMap.put("meta_state", 1);
        spaDocDao.updateJsonDoc(updateDocEvent.getId(), jsonMap);
    }
    
    @Subscribe
    public void deleteDocMeta(DeleteDocEvent deleteDocEvent)
    {
        spaDocDao.delete(deleteDocEvent.getResource_id());
    }
    
    @Subscribe
    public void updateFileThumbnail(UpdateFileThumbnailEvent event)
    {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("thumbnail", event.getThumbnail());
        spaFileDao.updateJsonDoc(event.getFile_id(), jsonMap);
    }
}
