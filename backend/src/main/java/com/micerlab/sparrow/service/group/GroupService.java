package com.micerlab.sparrow.service.group;

import com.micerlab.sparrow.domain.Result;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface GroupService {

    public Result createGroup(String user_id, Map<String, Object> paramMap);

    public Result getGroupMeta(String group_id);

    public Result updateGroupMeta(String group_id, Map<String, Object> paramMap);

    public Result deleteGroup(String group_id);

    public Result addGroupMember(String group_id, Map<String, Object> paramMap);

    public Result getGroupMember(String group_id);

    public Result deleteGroupMember(String group_id, String member_id);
}
