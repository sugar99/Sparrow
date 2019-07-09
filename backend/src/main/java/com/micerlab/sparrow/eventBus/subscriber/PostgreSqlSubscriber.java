package com.micerlab.sparrow.eventBus.subscriber;

import com.micerlab.sparrow.dao.postgre.GroupDao;
import com.micerlab.sparrow.dao.postgre.ResourceDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import com.micerlab.sparrow.domain.pojo.User;
import com.micerlab.sparrow.eventBus.event.doc.DeleteDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.InsertDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.UpdateDocEvent;
import com.micerlab.sparrow.eventBus.event.group.DeleteGroupEvent;
import com.micerlab.sparrow.eventBus.event.group.InsertGroupEvent;
import com.micerlab.sparrow.eventBus.event.group.UpdateGroupEvent;
import com.micerlab.sparrow.eventBus.event.user.InsertUserEvent;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.service.group.GroupService;
import com.micerlab.sparrow.service.resource.ResourceService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * PostgreSql 事件订阅者
 */
@Component("postgreSqlSubscriber")
@Transactional
public class PostgreSqlSubscriber {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ACLService aclService;

    public PostgreSqlSubscriber() {
        EventBus.getDefault().register(this);
    }

    /**
     * 新建用户
     * @param insertUserEvent 新建用户事件
     */
    @Subscribe
    public void insertUser(InsertUserEvent insertUserEvent) {
        String user_id = UUID.randomUUID().toString();
        User user = new User();
        user.setUser_id(user_id);
        user.setUsername(insertUserEvent.getUsername());
        user.setPassword(insertUserEvent.getPassword());
        user.setEmail(insertUserEvent.getEmail());
        user.setWork_no(insertUserEvent.getWork_no());
        //创建用户个人目录
        String personal_id = resourceService.createPersonalDir(user_id, insertUserEvent.getUsername());
        user.setPersonal_dir(personal_id);
        //创建个人群组
        String personal_group_id = groupService.createPersonalGroup(user_id, insertUserEvent.getUsername());
        user.setPersonal_group(personal_group_id);
        //个人群组对个人目录用可读可写权限
        aclService.updateGroupPermission(personal_group_id, personal_id, "110");
        userDao.createUser(user);
    }

    /**
     * 新建群组
     * @param insertGroupEvent 新建群组事件
     */
    @Subscribe
    public void insertGroup(InsertGroupEvent insertGroupEvent) {
        Group group = insertGroupEvent.getGroup();
        groupDao.createGroup(group);
    }

    /**
     * 更新群组元数据
     * @param updateGroupEvent 更新群组事件
     */
    @Subscribe
    public void updateGroup(UpdateGroupEvent updateGroupEvent) {
        groupDao.updateGroupMeta(updateGroupEvent.getGroup_id(), updateGroupEvent.getGroup_name(), updateGroupEvent.getGroup_desc());
    }

    /**
     * 删除群组
     * @param deleteGroupEvent 删除群组事件
     */
    @Subscribe
    public void deleteGroup(DeleteGroupEvent deleteGroupEvent) {
        String group_id = deleteGroupEvent.getGroup_id();
        //移除所有成员
        groupDao.removeAllMembers(group_id);
        //移除群组的所有权限
        groupDao.removeAllResource(group_id);
        //删除群组
        groupDao.deleteGroup(group_id);
    }

    /**
     * 新建文档
     * @param insertDocEvent 新建文档事件
     */
    @Subscribe
    public void insertDoc(InsertDocEvent insertDocEvent) {
        Resource resource = new Resource();
        resource.setResource_id(insertDocEvent.getResource_id());
        resource.setResource_name(insertDocEvent.getTitle());
        resource.setResource_type(insertDocEvent.getResource_type());
        resource.setCreator_id(insertDocEvent.getCreator());
        resource.setCreated_at(insertDocEvent.getCreated_time());
        resource.setThumbnail("./assets/images/doc.png");
        resourceDao.createResource(resource);
    }

    @Subscribe
    public void updateDoc(UpdateDocEvent updateDocEvent) {
        resourceDao.updateResourceMeta(updateDocEvent.getId(), updateDocEvent.getTitle());
    }

    /**
     * 删除文档
     * @param deleteDocEvent 删除文档事件
     */
    @Subscribe
    public void deleteDoc(DeleteDocEvent deleteDocEvent) {
        String resource_id = deleteDocEvent.getResource_id();
        //删除与父目录的关系
        resourceDao.deleteMasterSlaveRelation(resource_id);
        //删除文档与群组的所有权限关系
        aclService.deleteResourcePermission(resource_id);
        //删除文档
        resourceDao.deleteResource(resource_id);
    }
}
