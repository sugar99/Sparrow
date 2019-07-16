package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.config.ElasticsearchConfig;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.domain.search.SpaFilter;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import com.micerlab.sparrow.utils.BusinessException;
import org.elasticsearch.action.get.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SpaFileDao extends ESCRUDRepository<SpaFile>
{
    private Logger logger = LoggerFactory.getLogger(SpaFileDao.class);
    
    public SpaFileDao(ESBaseDao ESBaseDao, ElasticsearchConfig.Indices indices)
    {
        super(SpaFile.class, ESBaseDao, indices);
    }
    
    @Override
    protected String index()
    {
        return indices.getFile();
    }
    
    public List<SpaFilter> getSpaFilters(String file_id, SpaFilterType spaFilterType)
    {
        List<Map<String, Object>> spaFilterMaps = ESBaseDao.termsLookup(
                spaFilterType.sparrowIndex().getIndex(),
                index(), file_id, spaFilterType.getTypes());
        List<SpaFilter> spaFilters = new LinkedList<>();
        for (Map<String, Object> spaFilterMap : spaFilterMaps)
        {
            SpaFilter spaFilter = (new JSONObject(spaFilterMap)).toJavaObject(SpaFilter.class);
            spaFilters.add(spaFilter);
        }
        return spaFilters;
    }
    
    public void updateSpaFilters(String file_id, SpaFilterType spaFilterType, List<Long> spaFilterIds)
    {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put(spaFilterType.getTypes(), spaFilterIds);
        ESBaseDao.updateESDoc(index(), file_id, jsonMap);
    }
    
    public void updateThumbnail(String file_id, String thumbnail)
    {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("thumbnail", thumbnail);
        ESBaseDao.updateESDoc(index(), file_id, jsonMap);
    }
    
    public Map<String, Object> getDocAndParentFile(String doc_id, String parent_id)
    {
        Map<String, Object> result = new HashMap<>();
        if(StringUtils.isEmpty(parent_id))
        {
            result.put("parent", null);
            result.put("doc", ESBaseDao.getESDoc(SparrowIndex.SPA_DOCS.getIndex(), doc_id));
        }
        else {
            MultiGetRequest multiGetRequest = new MultiGetRequest();
            multiGetRequest.add(SparrowIndex.SPA_DOCS.getIndex(), doc_id);
            multiGetRequest.add(SparrowIndex.SPA_FILES.getIndex(), parent_id);
    
            try
            {
                MultiGetResponse multiGetResponse = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);
                MultiGetItemResponse[] itemResponses = multiGetResponse.getResponses();
                result.put("doc", itemResponses[0].getResponse().getSourceAsMap());
                result.put("parent", itemResponses[1].getResponse().getSource());
            } catch (IOException ex)
            {
                logger.error(ex.getMessage());
                ex.printStackTrace();
                throw new BusinessException(ErrorCode.SERVER_ERR_ELASTICSEARCH, ex.getMessage());
            }
        }
        return result;
    }
    
    
}
