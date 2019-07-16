package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.dao.postgre.*;
import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("aclService")
public class ACLServiceImpl implements ACLService{

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private ACLDao aclDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DirectoryDao directoryDao;

    @Autowired
    private DocumentDao documentDao;

    /**
     * 判断用户是否有权限做指定动作
     * @param user_id 用户id
     * @param resource_id 资源id
     * @param groupsIdList 用户的群组列表
     * @param action 动作
     * @return boolean
     */
    @Override
    public boolean hasPermission(String user_id, String resource_id, List<String> groupsIdList, ActionType action) {
        //管理员拥有所有权限
        if (user_id.equals(userDao.getAdminId())) {
            return true;
        }
        //获取对资源有操作权限的所有群组
        List<Group> hasPermissionGroupsList = resourceDao.getResourceGroups(resource_id);
        //O(n^2) 操作，后续需要改进
        for (Group group: hasPermissionGroupsList) {
            for (String groupId: groupsIdList) {
                if (groupId.equals(group.getGroup_id())) {
                    //用户所在群组对该目录有最高权限
                    String hasPermission = aclDao.getGroupPermission(groupId, resource_id);
                    if(hasPermission.equals("111")) {
                        return true;
                    }
                    //判断用户所在去之怒是否有对应权限
                    String actionCode = action.getActionCode();
                    for (int i = 0; i < 3; i++) {
                        if (actionCode.charAt(i) == '1' && hasPermission.charAt(i) == '1') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更新群组权限
     * @param group_id 群组id
     * @param resource_id 资源id
     * @param permission 权限
     */
    @Override
    public void updateGroupPermission(String group_id, String resource_id, String permission) {
        //新增权限
        if (aclDao.getGroupPermission(group_id, resource_id) == null) {
            aclDao.addGroupPermission(group_id, resource_id, permission);
        } else {
            //更新权限
            String permissionTemp = aclDao.getGroupPermission(group_id, resource_id);
            StringBuffer newPermission = new StringBuffer("000");
            for (int i = 0; i < 3; i++) {
                if (permission.charAt(i) == '1' || permissionTemp.charAt(i) == '1') {
                    newPermission.setCharAt(i, '1');
                }
            }
            aclDao.updateGroupPermission(group_id, resource_id, newPermission.toString());
        }
        //对该资源的上层目录有可读权限
        if (resourceDao.getMasterResourceMeta(resource_id) != null) {
            updateGroupPermission(group_id, resourceDao.getMasterResourceMeta(resource_id).getResource_id(), "100");
        }
    }

    @Override
    public Result addGroupPermission(String resource_id, Map<String, Object> paramMap) {
        String permission = paramMap.get("permission").toString();
        List<String> groupsIdList = (List<String>) paramMap.get("groups");
        //更新权限
        for (String group_id: groupsIdList) {
            updateGroupPermission(group_id, resource_id, permission);
        }
        return Result.OK().build();
    }

    /**
     * 删除群组权限
     * @param group_id 群组id
     * @param resource_id 资源id
     */
    @Override
    public Result deleteGroupPermission(String group_id, String resource_id) {
        aclDao.deleteGroupPermission(group_id, resource_id);
        return Result.OK().build();
    }

    /**
     * 获取对指定资源有操作权限的所有群组及其权限
     * @param user_id 用户id
     * @param resource_id 资源id
     * @return Result (data: groupList)
     */

    @Override
    public Result getAuthGroups(String user_id, String resource_id, String type) {
        List<Group> authGroupsList = type.equals("dir")? directoryDao.getAuthGroups(resource_id)
                    : documentDao.getAuthGroups(resource_id);
        String creator_id = type.equals("dir")? directoryDao.getDir(resource_id).getCreator_id()
                    : documentDao.getDoc(resource_id).getCreator_id();
//        String creator_id = resourceDao.getResourceMeta(resource_id).getCreator_id();
        Map<String, Object> data = new HashMap<>();
        //接口调用者是否为资源的创建者，前端根据此标志进行渲染
        data.put("isOwner", user_id.equals(creator_id)? 1 : 0);
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
        data.put("groupList", groupList);
        return Result.OK().data(data).build();
    }

    /**
     * 删除资源所有的群组权限关系
     * @param resource_id 资源id
     */
    @Override
    public void deleteResourcePermission(String resource_id) {
        aclDao.deleteResourceAllPermission(resource_id);
    }
}
