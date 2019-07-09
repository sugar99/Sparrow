package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SearchUserGroupDao {

    private RestHighLevelClient elasticsearchClient;

    public SearchUserGroupDao(RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Map<String, Object>> search(String keyword, String index, int size) throws IOException{
        SearchRequest searchRequest = new SearchRequest();
        //指定index
        searchRequest.indices(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //match字段“name”，返回指定size个结果
        sourceBuilder.query(QueryBuilders.matchQuery("name", keyword)).size(size);
        searchRequest.source(sourceBuilder);
        SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        //格式返回结果
        SearchHit[] searchHits = response.getHits().getHits();
        List<Map<String, Object>> suggested_user = new LinkedList<>();
        for (SearchHit hit: searchHits) {
            Map<String, Object> user = hit.getSourceAsMap();
            user.put("score", hit.getScore());
            suggested_user.add(user);
        }
        return suggested_user;
    }
}
