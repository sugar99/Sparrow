package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CUDUserGroupDao {

    private RestHighLevelClient elasticsearchClient;

    @Autowired
    private ESConfig.Indices sparrowIndices;

    public CUDUserGroupDao(RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    /**
     * 新增用户信息
     * @param user_id 用户id
     * @param username 用户名
     * @param work_no 工号
     */
    public void insertUser(String user_id, String username, String work_no) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("name", username);
        jsonMap.put("work_no", work_no);
        IndexRequest indexRequest = new IndexRequest(sparrowIndices.getUser()).id(user_id).source(jsonMap);

        try {
            IndexResponse response = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }

    /**
     * 新增群组信息
     * @param group_id 群组id
     * @param group_name 群组
     * @param group_desc 群组描述
     * @param creator 创建者
     */
    public void insertGroup(String group_id, String group_name, String group_desc, String creator) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("name", group_name);
        jsonMap.put("desc", group_desc);
        jsonMap.put("creator", creator);
        IndexRequest indexRequest = new IndexRequest(sparrowIndices.getGroup()).id(group_id).source(jsonMap);

        try {
            IndexResponse response = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }

    public void updateGroup(String group_id, String group_name, String group_desc) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("name", group_name);
        jsonMap.put("desc", group_desc);
        UpdateRequest updateRequest = new UpdateRequest(sparrowIndices.getGroup(), group_id).doc(jsonMap);

        try {
            UpdateResponse response = elasticsearchClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }

    public void deleteGroup(String group_id) {
        DeleteRequest deleteRequest = new DeleteRequest(sparrowIndices.getGroup(), group_id);
        try {
            DeleteResponse response = elasticsearchClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
        }
    }
}
