package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.domain.Result;
import javafx.beans.binding.ObjectExpression;

import java.util.Map;

public interface ResourceService {

    public Result createResource(String type, String cur_id);

    public Result getResourceMeta(String resource_id);

    public Result updateResourceMeta(String resource_id, Map<String, Object> paramMap);

    public Result deleteResource(String resource_id);

    public Result getSlavesResource(String resource_id);

    public Result getAuthGroups(String resource_id);

    public Result addPermission(String resource_id, Map<String, Object> paramMap);

    public Result removePermission(String resource_id, Map<String, Object> paramMap);

}
