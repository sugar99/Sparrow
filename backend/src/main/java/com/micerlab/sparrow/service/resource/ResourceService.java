package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.domain.Result;
import javafx.beans.binding.ObjectExpression;

import java.util.Map;

public interface ResourceService {

    Result createResource(String user_id, String cur_id, String type);

    String createPersonalDir(String user_id, String username);

    Result getDirMeta(String dir_id);

    String getCreatorId(String resource_id);

    String getMasterDirId(String resource_id);

    Result updateDirMeta(String dir_id, Map<String, Object> paramMap);

    Result deleteResource(String resource_id, String type);

    Result getSlavesResource(String user_id, String resource_id, String type);

    Result getAuthGroups(String user_id, String resource_id);

    Result addPermission(String resource_id, Map<String, Object> paramMap);

    Result removePermission(String resource_id, Map<String, Object> paramMap);

    Result getDocMeta(String doc_id);

    Result updateDocMeta(String doc_id, Map<String, Object> parms);

}
