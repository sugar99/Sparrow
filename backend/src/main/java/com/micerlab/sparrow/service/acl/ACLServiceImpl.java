package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.dao.postgre.*;
import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.ResourceType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.UpdateAuthGroupsParams;
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
    public boolean hasPermission(String user_id, String resource_id, ResourceType resourceType, List<String> groupsIdList, ActionType action) {
        //管理员拥有所有权限
        if (user_id.equals(userDao.getAdminId())) {
            return true;
        }
        //获取对资源有操作权限的所有群组
        List<Group> hasPermissionGroupsList = new ArrayList<>();
        switch (resourceType.getType()) {
            case "dir":
                hasPermissionGroupsList = directoryDao.getAuthGroups(resource_id);
                break;
            case "doc":
                hasPermissionGroupsList = documentDao.getAuthGroups(resource_id);
                break;
        }
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
    public void updateGroupPermission(String group_id, String resource_id, ResourceType resourceType, String permission) {
        //新增权限
        if (aclDao.getGroupPermission(group_id, resource_id) == null) {
            aclDao.addGroupPermission(group_id, resource_id, permission);
        } else {
            //更新权限
            String permissionTemp = aclDao.getGroupPermission(group_id, resource_id);
            StringBuilder newPermission = new StringBuilder("000");
            for (int i = 0; i < 3; i++) {
                if (permission.charAt(i) == '1' || permissionTemp.charAt(i) == '1') {
                    newPermission.setCharAt(i, '1');
                }
            }
            aclDao.updateGroupPermission(group_id, resource_id, newPermission.toString());
        }
        switch (resourceType.getType()) {
            case "dir":
                if (directoryDao.getMasterDir(resource_id) != null) {
                    updateGroupPermission(group_id, directoryDao.getMasterDir(resource_id).getId(), ResourceType.DIR, "100");
                }
                break;
            case "doc":
                if (documentDao.getMasterDir(resource_id) != null) {
                    updateGroupPermission(group_id, documentDao.getMasterDir(resource_id).getId(), ResourceType.DOC, "100");
                }
                break;
        }
    }

    @Override
    public Result addGroupPermission(String resource_id, ResourceType resourceType, UpdateAuthGroupsParams params) {
        List<String> groupsIdList = params.getGroups();
        //更新权限
        for (String group_id: groupsIdList) {
            updateGroupPermission(group_id, resource_id, resourceType, params.getPermission());
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
        List<Group> authGroupsList = new ArrayList<>();
        String creator_id = "";
        switch (type) {
            case "dir":
                authGroupsList = directoryDao.getAuthGroups(resource_id);
                creator_id = directoryDao.getDir(resource_id).getCreator_id();
                break;
            case "doc":
                authGroupsList = documentDao.getAuthGroups(resource_id);
                creator_id = documentDao.getDoc(resource_id).getCreator_id();
                break;
        }
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
