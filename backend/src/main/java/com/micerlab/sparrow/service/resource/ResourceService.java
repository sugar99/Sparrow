package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.domain.Result;
import javafx.beans.binding.ObjectExpression;

import java.util.Map;

public interface ResourceService {

    Result createResource(String type, String cur_id);

    Result getResourceMeta(String resource_id);

    Result updateResourceMeta(String resource_id, Map<String, Object> paramMap);

    Result deleteResource(String resource_id);

    Result getSlavesResource(String resource_id);

    Result getAuthGroups(String resource_id);

    Result addPermission(String resource_id, Map<String, Object> paramMap);

    Result removePermission(String resource_id, Map<String, Object> paramMap);

    Result retrieveDocMeta(String doc_id);

    Result updateDocMeta(String doc_id, Map<String, Object> parms);

}
