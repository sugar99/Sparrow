package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.domain.search.SpaFilter;
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
public class SpaFilterDao
{
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    @Autowired
    private ESBaseDao ESBaseDao;
    
    public List<Map<String, Object>> search(SpaFilterType spaFilterType, String keyword, int size) throws IOException
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
    
    public SpaFilter getSpaFilter(SpaFilterType spaFilterType, String filter_id)
    {
        Map<String, Object> ESDoc = ESBaseDao.getESDoc(spaFilterType.sparrowIndex().getIndex(),filter_id);
        if(ESDoc == null)
            return null;
        SpaFilter spaFilter = (new JSONObject(ESDoc)).toJavaObject(SpaFilter.class);
        return spaFilter;
    }
    
    public void updateSpaFilter(SpaFilterType spaFilterType, String filter_id, SpaFilter spaFilter)
    {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(spaFilter);
        ESBaseDao.updateESDoc(spaFilterType.sparrowIndex().getIndex(), filter_id, jsonObject);
    }
    
    public void deleteSpaFilter(SpaFilterType spaFilterType, String filter_id)
    {
        ESBaseDao.deleteESDoc(spaFilterType.sparrowIndex().getIndex(), filter_id);
    }
}
