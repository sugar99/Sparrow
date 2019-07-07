package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.domain.ActionType;

import java.util.List;

public interface ACLService {
    public boolean hasPermission(String user_id, String resource_id, List<String> groupsIdList, ActionType action);

    public void updateGroupPermission(String group_id, String resource_id, String permission);

    public void deleteGroupPermission(String group_id, String resource_id);

    public boolean deleteResourcePermission(String user_id, String resource_id);
}
