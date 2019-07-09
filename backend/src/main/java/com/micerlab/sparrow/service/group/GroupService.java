package com.micerlab.sparrow.service.group;

import com.micerlab.sparrow.domain.Result;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface GroupService {

    Result createGroup(String user_id, Map<String, Object> paramMap);

    String createPersonalGroup(String user_id, String username);

    Result getGroupMeta(String group_id);

    String getGroupOwnerId(String group_id);

    Result updateGroupMeta(String group_id, Map<String, Object> paramMap);

    Result deleteGroup(String group_id);

    Result addGroupMember(String group_id, Map<String, Object> paramMap);

    Result getGroupMember(String user_id, String group_id);

    Result deleteGroupMember(String group_id, String member_id);

    Result getAuthResource(String group_id);
}
