package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;

import java.util.Map;

@Deprecated
public interface ResourceService {

    Result createResource(String user_id, String cur_id, String type);

    String createPersonalDir(String user_id, String username);

    Result getDirMeta(String dir_id);

    String getCreatorId(String resource_id);

    String getMasterDirId(String resource_id);

    Result updateDirMeta(String dir_id, Map<String, Object> paramMap);

    Result deleteResource(String resource_id, String type);

    Result getSlavesResource(String user_id, String resource_id, String type);

    Result addPermission(String resource_id, Map<String, Object> paramMap);

    Result removePermission(String resource_id, Map<String, Object> paramMap);

    Result retrieveDocMeta(String doc_id);

    Result updateDocMeta(String doc_id, SpaDocUpdateParams params);
    
    /**
     * 获取文档包含的所有文件的Meta
     */
    Result getFiles(String doc_id);
}
