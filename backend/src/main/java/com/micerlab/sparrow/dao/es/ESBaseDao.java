package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
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
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
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
public class ESBaseDao
{
    private Logger logger = LoggerFactory.getLogger(ESBaseDao.class);
    
    // TODO: 用动态代理统一处理ES异常
//    public void handleESException(IOException ex)
//            throws BusinessException
//    {
//        logger.error(ex.getMessage());
//        ex.printStackTrace();
//        throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
//    }
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    public RestHighLevelClient getRestHighLevelClient()
    {
        return restHighLevelClient;
    }
    
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
     * @return  1.the json map of the doc
     *          2.null if the specific doc doesn't exist
     */
    public JSONObject getESDoc(String index, String id)
    {
        try
        {
            GetRequest getRequest = new GetRequest(index, id);
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            return new JSONObject(getResponse.getSourceAsMap());
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    /**
     * 创建ES文档
     * @param index ES索引
     * @param jsonMap 用json表示的ES文档
     * @return 创建ES文档的id（uuid）
     */
    public String createESDoc(String index, JSONObject jsonMap)
    {
        try
        {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(jsonMap);
            logger.debug("created ES doc: index = " + index + ";doc = "  + jsonMap.toString());
            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return indexResponse.getId();
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    /**
     * 索引存储ES文档对象
     * @param index ES索引
     * @param id ES文档id
     * @param jsonMap 用json表示的ES文档
     * @param overwriteIfExist 如果包含该id的ES文档已存在，是否覆盖原有ES文档
     *                1.overwriteIfExist = true：  覆盖原有ES文档
     *                2.overwriteIfExist = false： 不覆盖
     * @return 1.DocWriteResponse.Result.CREATED 创建了新的ES文档
     *         2.DocWriteResponse.Result.UPDATED 更新了原有ES文档
     */
    public DocWriteResponse.Result indexESDoc(String index, String id, JSONObject jsonMap, boolean overwriteIfExist)
    {
        try
        {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.id(id);
            indexRequest.source(jsonMap);
            logger.debug("index ES doc: index = " + index + ";id = " + id + "; doc = "  + jsonMap.toString());
            indexRequest.create(!overwriteIfExist);
            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return indexResponse.getResult();
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    /**
     * 更新ES文档
     * @param index ES索引
     * @param id ES文档id
     * @param jsonMap 用json表示的ES文档
     * @return 更新后的ES文档
     */
    public JSONObject updateESDoc(String index, String id, JSONObject jsonMap)
    {
        try
        {
            UpdateRequest updateRequest = new UpdateRequest(index, id);
            updateRequest.doc(jsonMap);
            updateRequest.docAsUpsert(true); // 若ES文档不存在，则创建新的
            logger.debug("update ES doc: index = " + index + ";id = " + id + "; doc = "  + jsonMap.toString());
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//            if(!updateResponse.getResult().equals(DocWriteResponse.Result.UPDATED))
//                throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH,
//                        "ES文档更新失败:/" + index + "/" + id
//                                + ";" + updateResponse.toString());
            return new JSONObject(updateResponse.getGetResult().sourceAsMap());
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    /**
     * 删除ES文档
     * @param index ES索引
     * @param id ES文档id
     * @return 删除结果的响应
     */
    public DeleteResponse deleteESDoc(String index, String id)
    {
        try
        {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            String msg = "index = " + index + ";id = " + id;
            logger.info("delete ES doc: " + msg);
            logger.info(deleteResponse.toString());
            if(deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND)
                logger.error("待删除ES文档不存在：" + msg);
            else if (shardInfo.getTotal() != shardInfo.getSuccessful())
                logger.error("未完全删除ES文档：" + msg);
            return deleteResponse;
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
}
