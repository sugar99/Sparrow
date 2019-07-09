package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.dao.postgre.ACLDao;
import com.micerlab.sparrow.dao.postgre.ResourceDao;
import com.micerlab.sparrow.dao.postgre.UserDao;
import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.pojo.Group;
import com.micerlab.sparrow.domain.pojo.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 删除群组权限
     * @param group_id 群组id
     * @param resource_id 资源id
     */
    @Override
    public void deleteGroupPermission(String group_id, String resource_id) {
        aclDao.deleteGroupPermission(group_id, resource_id);
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
