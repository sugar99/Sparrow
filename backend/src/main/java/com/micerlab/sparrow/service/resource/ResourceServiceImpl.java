package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.postgre.ACLDao;
import com.micerlab.sparrow.dao.postgre.ResourceDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;
import org.springframework.beans.factory.annotation.Autowired;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import com.micerlab.sparrow.domain.pojo.User;
import com.micerlab.sparrow.eventBus.event.doc.DeleteDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.InsertDocEvent;
import com.micerlab.sparrow.eventBus.event.doc.UpdateDocEvent;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.utils.TimeUtil;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.util.*;

@Service("resourceService")
@Transactional
public class ResourceServiceImpl implements ResourceService{

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ACLDao aclDao;

    @Autowired
    private ACLService aclService;

    /**
     * 新建资源
     * @param user_id 用户id
     * @param type 资源类型
     * @param cur_id 当前目录id
     * @return Result (data: resource)
     */
    @Override
    public Result createResource(String user_id, String cur_id, String type) {
        String resource_id = UUID.randomUUID().toString();
        Resource resource = new Resource();
        if (type.equals("doc")) {
            //新建文档
            Timestamp timestamp = TimeUtil.currentTime();
            // 新建文档事件订阅者 (ES) ✓
            //产生InsertDocEvent,在ES和PostgreSQL中同步文档的元数据
            EventBus.getDefault().post(new InsertDocEvent(resource_id, "doc", user_id, timestamp, timestamp));
        } else {
            //新建目录
            resource.setResource_id(resource_id);
            resource.setResource_type("dir");
            resource.setCreator_id(user_id);
            resource.setCreated_at(TimeUtil.currentTime());
            resource.setThumbnail("./assets/images/docCnt.png");
            resourceDao.createResource(resource);
        }
        //更新父子目录表
        resourceDao.createMasterSlaveRelation(cur_id, resource_id);
        //默认创建者对创建的资源有可读权限；personal_group_id 创建者个人群组
        String personal_group_id = userDao.getUserMetaById(user_id).getPersonal_group();
        aclService.updateGroupPermission(personal_group_id, resource_id, "100");
        return Result.OK().data(resource).build();
    }

    /**
     * 新建个人目录（新建用户时为用户新建个人目录作为默认工作区）
     * @param user_id 用户id
     * @param username 用户名 （作为目录名称）
     * @return 目录id
     */
    @Override
    public String createPersonalDir(String user_id, String username) {
        //创建目录
        String resource_id = UUID.randomUUID().toString();
        Resource resource = new Resource();
        resource.setResource_id(resource_id);
        resource.setResource_name(username);
        resource.setResource_type("dir");
        resource.setCreated_at(TimeUtil.currentTime());
        resource.setCreator_id(userDao.getAdminId());
        resourceDao.createResource(resource);
        //该目录在home目录下
        String master_id = resourceDao.getHomeDirId();
        resourceDao.createMasterSlaveRelation(master_id, resource_id);
        return resource_id;
    }

    /**
     * 返回目录元数据
     * @param dir_id 目录id
     * @return Result (data: resource)
     */
    @Override
    public Result getDirMeta(String dir_id)
    {
        return Result.OK().data(resourceDao.getResourceMeta(dir_id)).build();
    }

    /**
     * 返回资源创建者id
     * @param resource_id 资源id
     * @return 创建者id
     */
    @Override
    public String getCreatorId(String resource_id) {
        return resourceDao.getResourceMeta(resource_id).getCreator_id();
    }

    /**
     * 返回父目录id
     * @param resource_id 资源id
     * @return 父目录id
     */
    @Override
    public String getMasterDirId(String resource_id) {
        return resourceDao.getMasterResourceMeta(resource_id).getResource_id();
    }

    /**
     * 更新目录元数据
     * @param dir_id 目录id
     * @param paramMap 参数
     * @return Result (data: null)
     */
    @Override
    public Result updateDirMeta(String dir_id, Map<String, Object> paramMap)
    {
        String dir_name = paramMap.get("dir_name").toString();
        //更新目录名称
        resourceDao.updateResourceMeta(dir_id, dir_name);
        return Result.OK().build();
    }

    /**
     * 删除资源
     * @param resource_id 资源id
     * @return Result
     */
    @Override
    public Result deleteResource(String resource_id, String type) {
        if (type.equals("doc")) {
            // 删除文档事件订阅者
            //产生DeleteDocEvent,在ES和PostgreSQL中同步删除文档
            EventBus.getDefault().post(new DeleteDocEvent(resource_id));
        } else {
            //删除目录下所有资源
            List<String> slaveResourcesIdList = resourceDao.getTotalSlaveResourcesId(resource_id);
            if (slaveResourcesIdList != null) {
                for (String slave_id: slaveResourcesIdList) {
                    resourceDao.deleteMasterSlaveRelation(slave_id);
                    aclService.deleteResourcePermission(slave_id);
                    resourceDao.deleteResource(resource_id);
                }
            }
            //删除目录
            resourceDao.deleteMasterSlaveRelation(resource_id);
            aclService.deleteResourcePermission(resource_id);
            resourceDao.deleteResource(resource_id);
        }
        return Result.OK().build();
    }

    /**
     * 获取子资源信息
     * @param user_id 用户id
     * @param resource_id 资源id
     * @return Result (data: resourceList)
     */
    @Override
    public Result getSlavesResource(String user_id, String resource_id, String type) {
        if (type.equals("doc")) {
            //TODO 获取文档下的所有文件 (ES)
            return Result.OK().build();
        } else {
            //获取指定目录的子资源（一级）
            List<Map<String, Object>> resourceList = resourceDao.getSlaveResources(resource_id);
            return Result.OK().data(resourceList).build();
        }
    }

    /**
     * 获取对指定资源有操作权限的所有群组及其权限
     * @param user_id 用户id
     * @param resource_id 资源id
     * @return Result (data: groupList)
     */
    @Override
    public Result getAuthGroups(String user_id, String resource_id) {
        List<Group> authGroupsList = resourceDao.getResourceGroups(resource_id);
        Map<String, Object> resultMap = new HashMap<>();
        //接口调用者是否为资源的创建者，前端根据此标志进行渲染
        resultMap.put("isOwner", user_id.equals(resourceDao.getResourceMeta(resource_id).getCreator_id())? 1 : 0);
        List<Map<String, Object>> groupList = new ArrayList<>();
        if (authGroupsList!= null) {
            for (Group group : authGroupsList) {
                //获取群组信息及操作权限
                Map<String, Object> perGroup = new HashMap<>();
                perGroup.put("groupInfo", group);
                perGroup.put("permission", aclDao.getGroupPermission(group.getGroup_id(), resource_id));
                groupList.add(perGroup);
            }
        }
        resultMap.put("groupList", groupList);
        return Result.OK().data(resultMap).build();
    }

    /**
     * 添加群组对指定目录或文档的操作权限
     * @param resource_id 资源id
     * @param paramMap 参数
     * @return Result (data: null)
     */
    @Override
    public Result addPermission(String resource_id, Map<String, Object> paramMap) {
        String permission = paramMap.get("permission").toString();
        List<String> groupsIdList = (List<String>) paramMap.get("groupsIdList");
        //更新权限
        for (String group_id: groupsIdList) {
            aclService.updateGroupPermission(group_id, resource_id, permission);
        }
        return Result.OK().build();
    }

    /**
     * 撤销群组对指定目录或文档的操作权限
     * @param resource_id 资源id
     * @param paramMap 参数
     * @return Result (data: null)
     */
    @Override
    public Result removePermission(String resource_id, Map<String, Object> paramMap) {
        String group_id = paramMap.get("group_id").toString();
        //更新权限
        aclService.deleteGroupPermission(group_id, resource_id);
        return Result.OK().build();
    }

    @Autowired
    private SpaDocDao spaDocDao;
    
    @Override
    public Result retrieveDocMeta(String doc_id)
    {
        Map<String, Object> docMeta = spaDocDao.retrieveDocMeta(doc_id);
        return Result.OK().data(docMeta).build();
    }

    @Override
    public Result updateDocMeta(String doc_id, SpaDocUpdateParams params)
    {
        // 更新文档元数据的事件订阅者
        String title = params.getTitle();
        String desc = params.getDesc();
        Timestamp modified_time = TimeUtil.currentTime();
        EventBus.getDefault().post(new UpdateDocEvent(doc_id, title, desc, modified_time));
        
        // 调用ES / Postgre 订阅者处理
        return Result.OK().build();
    }
    
    @Override
    public Result getFiles(String doc_id)
    {
        List<Map<String, Object>> files = spaDocDao.getFiles(doc_id);
        return Result.OK().data(files).build();
    }
}
