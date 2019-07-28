package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.ResourceType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.UpdateAuthGroupsParams;

import java.util.List;
import java.util.Map;


//Access Control List 进行访问控制
public interface ACLService {
    //通过比对确认用户是否有该权限
    boolean hasPermission(String user_id, String resource_id, ResourceType resourceType, List<String> groupsIdList, ActionType action);
    //
    void updateGroupPermission(String group_id, String resource_id, ResourceType resourceType, String permission);

    Result addGroupPermission(String resource_id, ResourceType resourceType, UpdateAuthGroupsParams params);

    Result deleteGroupPermission(String group_id, String resource_id);

    Result getAuthGroups(String user_id, String resource_id, String type);

    void deleteResourcePermission(String resource_id);

}
