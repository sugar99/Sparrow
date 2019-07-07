package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.domain.Result;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService{

    @Override
    public Result createResource(String type, String cur_id) {
        return null;
    }

    @Override
    public Result getResourceMeta(String resource_id) {
        return null;
    }

    @Override
    public Result updateResourceMeta(String resource_id, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result deleteResource(String resource_id) {
        return null;
    }

    @Override
    public Result getSlavesResource(String resource_id) {
        return null;
    }

    @Override
    public Result getAuthGroups(String resource_id) {
        return null;
    }

    @Override
    public Result addPermission(String resource_id, Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public Result removePermission(String resource_id, Map<String, Object> paramMap) {
        return null;
    }
}
