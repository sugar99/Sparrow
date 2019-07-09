package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.indices.TermsLookup;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ElasticsearchBaseDao
{
    private Logger logger = LoggerFactory.getLogger(ElasticsearchBaseDao.class);
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    public List<Map<String, Object>> termsLookup(
            String search_index,
            String index,
            String id,
            String path
    )
    {
        SearchTemplateRequest request = new SearchTemplateRequest();
        request.setRequest(new SearchRequest(search_index));
        
        request.setScriptType(ScriptType.INLINE);
        request.setScript("{" +
                "  \"query\": {" +
                "    \"terms\": {" +
                "      \"id\": {" +
                "        \"index\": \"{{index}}\"," +
                "        \"id\": \"{{id}}\"," +
                "        \"path\": \"{{path}}\"" +
                "      }" +
                "    }" +
                "  }" +
                "}");
        
        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put("index", index);
        scriptParams.put("id", id);
        scriptParams.put("path", path);
        
        request.setScriptParams(scriptParams);
        try
        {
            SearchTemplateResponse response = restHighLevelClient.searchTemplate(request, RequestOptions.DEFAULT);
            SearchResponse searchResponse = response.getResponse();
            List<Map<String, Object>> results = new LinkedList<>();
            for (SearchHit searchHit : searchResponse.getHits().getHits())
                results.add(searchHit.getSourceAsMap());
            return results;
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    public SearchResponse search(SearchRequest searchRequest)
    {
        try
        {
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    /**
     * 获取ES文档对象
     * @param index ES索引
     * @param id ES文档id
     * @return null if the doc doesn't exist
     */
    public Map<String, Object> getESDoc(String index, String id)
    {
        try
        {
            GetRequest getRequest = new GetRequest(index, id);
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            return (getResponse.isExists()) ? getResponse.getSourceAsMap() : null;
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    public void indexESDoc(String index, String id, Map<String, Object> docMap)
    {
        try
        {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.id(id);
            indexRequest.source(docMap);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if(indexResponse.getResult() != DocWriteResponse.Result.CREATED)
                throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH,
                        "ES文档创建失败:/" + index + "/" + id
                                + ";" + indexResponse.toString());
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    public void updateESDoc(String index, String id, Map<String, Object> docMap)
    {
        try
        {
            UpdateRequest updateRequest = new UpdateRequest(index, id);
            logger.debug("update doc : " + docMap.toString());
            updateRequest.doc(docMap);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            if(!updateResponse.getResult().equals(DocWriteResponse.Result.UPDATED))
                throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH,
                        "ES文档更新失败:/" + index + "/" + id
                                + ";" + updateResponse.toString());
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    public boolean deleteESDoc(String index, String id)
    {
        try
        {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            logger.info("delete ES doc: /" + index + "/" + id);
            logger.info(deleteResponse.toString());
            return deleteResponse.getResult() != DocWriteResponse.Result.NOT_FOUND &&
                    shardInfo.getTotal() == shardInfo.getSuccessful();
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
}
