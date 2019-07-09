package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.domain.search.SpaFilterType;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SearchSpaFilterDao
{
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    
    public List<Map<String, Object>> searchSpaFilters(SpaFilterType spaFilterType, String keyword, int size) throws IOException
    {
        SearchRequest request = new SearchRequest(spaFilterType.sparrowIndex().getIndex());
    
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(size);
        if(!StringUtils.isEmpty(keyword))
        {
            Map<String, Float> fields = new HashMap<>();
            fields.put("title", 1f);
            fields.put("title.cn", 3f);
            fields.put("desc", 1f);
            fields.put("desc.cn", 1.2f);
    
            searchSourceBuilder.query(
                    QueryBuilders.multiMatchQuery(keyword ).fields(fields)
            );
        }
        request.source(searchSourceBuilder);
    
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> spaFilters = new LinkedList<>();
        for(SearchHit searchHit: response.getHits().getHits())
            spaFilters.add(searchHit.getSourceAsMap());
        
        return spaFilters;
    }
}
