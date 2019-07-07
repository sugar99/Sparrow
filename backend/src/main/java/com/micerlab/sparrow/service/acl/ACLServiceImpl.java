package com.micerlab.sparrow.service.acl;

import com.micerlab.sparrow.domain.ActionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("aclService")
public class ACLServiceImpl implements ACLService{
    @Override
    public boolean hasPermission(String user_id, String resource_id, List<String> groupsIdList, ActionType action) {
        return false;
    }

    @Override
    public void updateGroupPermission(String group_id, String resource_id, String permission) {

    }

    @Override
    public void deleteGroupPermission(String group_id, String resource_id) {

    }

    @Override
    public boolean deleteResourcePermission(String user_id, String resource_id) {
        return false;
    }
}
