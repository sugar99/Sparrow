package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.doc.SpaDoc;
import com.micerlab.sparrow.utils.BusinessException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpaDocDao
{
    private final static String index = SparrowIndex.SPA_DOCS.getIndex();
    
    private Logger logger = LoggerFactory.getLogger(SpaDocDao.class);
    
    @Autowired
    private ElasticsearchBaseDao elasticsearchBaseDao;
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    public List<Map<String, Object>> getFiles(String doc_id)
    {
        List<Map<String, Object>> files = elasticsearchBaseDao.termsLookup(
                SparrowIndex.SPA_FILES.getIndex(),
                SparrowIndex.SPA_DOCS.getIndex(),
                doc_id,
                "files"
        );
        for(Map<String, Object> file: files)
            file.put("resource_type", "file");
        return files;
    }
    
    public Map<String, Object> retrieveDocMeta(String doc_id)
    {
        GetRequest getRequest = new GetRequest(index, doc_id);
        try
        {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            return getResponse.getSourceAsMap();
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    public void updateDocMeta(String doc_id, Map<String, Object> docMap)
    {
        elasticsearchBaseDao.updateESDoc(index, doc_id, docMap);
    }
    
    public void createDocMeta(SpaDoc spaDoc)
    {
        JSONObject srcMap = (JSONObject) JSONObject.toJSON(spaDoc);
        elasticsearchBaseDao.indexESDoc(index,spaDoc.getId(), srcMap);
    }
    
    public void deleteDocMeta(String doc_id)
    {
        elasticsearchBaseDao.deleteESDoc(index, doc_id);
    }
    
    
}
