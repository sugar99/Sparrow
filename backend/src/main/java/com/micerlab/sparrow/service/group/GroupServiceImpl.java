package com.micerlab.sparrow.service.group;

import com.micerlab.sparrow.domain.Result;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("groupService")
public class GroupServiceImpl implements GroupService{

    @Override
    public Result createGroup(String user_id, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result getGroupMeta(String group_id) {
        return null;
    }

    @Override
    public Result updateGroupMeta(String group_id, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result deleteGroup(String group_id) {
        return null;
    }

    @Override
    public Result addGroupMember(String group_id, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result getGroupMember(String group_id) {
        return null;
    }

    @Override
    public Result deleteGroupMember(String group_id, String member_id) {
        return null;
    }
}
