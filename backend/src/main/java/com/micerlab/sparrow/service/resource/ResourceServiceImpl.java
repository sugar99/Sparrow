package com.micerlab.sparrow.service.resource;

import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.doc.SpaDocUpdateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    private SpaDocDao spaDocDao;
    
    @Override
    public Result retrieveDocMeta(String doc_id)
    {
        Map<String, Object> docMeta = spaDocDao.retrieveDocMeta(doc_id);
        return Result.OK().data(docMeta).build();
    }

    @Override
    public Result updateDocMeta(String doc_id, SpaDocUpdateParams params)
    {
        Map<String, Object> jsonMap = params.toMap();
        // TODO: current time
        String modified_time = "";
        jsonMap.put("modified_time", modified_time);
        jsonMap.put("meta_state", 1);
        spaDocDao.updateDocMeta(doc_id, jsonMap);
        return Result.OK().build();
    }
    
    @Override
    public Result getFiles(String doc_id)
    {
        List<Map<String, Object>> files = spaDocDao.getFiles(doc_id);
        return Result.OK().data(files).build();
    }
}
