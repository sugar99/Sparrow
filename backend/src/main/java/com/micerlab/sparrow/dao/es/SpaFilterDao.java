package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.domain.meta.SpaFilter;
import com.micerlab.sparrow.domain.meta.SpaFilterType;
import com.micerlab.sparrow.utils.MapUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SpaFilterDao
{
    @Autowired
    private ESBaseDao ESBaseDao;
    
    @Autowired
    private ESConfig.Indices sparrowIndices;
    
    public List<Map<String, Object>> search(SpaFilterType spaFilterType, String keyword, int size)
    {
        SearchRequest request = new SearchRequest(sparrowIndices.spaFilterIndex(spaFilterType));
        
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(size);
        if (!StringUtils.isEmpty(keyword))
        {
            Map<String, Float> fields = new HashMap<>();
            fields.put("title", 1f);
            fields.put("title.cn", 3f);
            fields.put("desc", 1f);
            fields.put("desc.cn", 1.2f);
            
            searchSourceBuilder.query(
                    QueryBuilders.multiMatchQuery(keyword).fields(fields)
            );
        }
        request.source(searchSourceBuilder);
        
        SearchResponse response = ESBaseDao.search(request);
        List<Map<String, Object>> spaFilters = new LinkedList<>();
        for (SearchHit searchHit : response.getHits().getHits())
            spaFilters.add(searchHit.getSourceAsMap());
        
        return spaFilters;
    }
    
    public SpaFilter get(SpaFilterType spaFilterType, String filter_id)
    {
        JSONObject ESDoc = ESBaseDao.getESDoc(sparrowIndices.spaFilterIndex(spaFilterType), filter_id);
        return MapUtils.jsonMap2Obj(ESDoc, SpaFilter.class);
    }
    
    public void update(SpaFilterType spaFilterType, String filter_id, SpaFilter spaFilter)
    {
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(spaFilter);
        ESBaseDao.updateESDoc(sparrowIndices.spaFilterIndex(spaFilterType), filter_id, jsonObject);
    }
    
    public void delete(SpaFilterType spaFilterType, String filter_id)
    {
        ESBaseDao.deleteESDoc(sparrowIndices.spaFilterIndex(spaFilterType), filter_id);
    }
}
