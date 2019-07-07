package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.domain.ActionType;

import java.util.List;

public interface ACLService {
    boolean hasPermission(String user_id, String resource_id, List<String> groupsIdList, ActionType action);

    void updateGroupPermission(String group_id, String resource_id, String permission);

    void deleteGroupPermission(String group_id, String resource_id);

    boolean deleteResourcePermission(String user_id, String resource_id);
}
