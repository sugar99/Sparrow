package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.file.SpaFile;
import com.micerlab.sparrow.utils.BusinessException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class SpaFileDao
{
    private Logger logger = LoggerFactory.getLogger(SpaFileDao.class);
    
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    @Autowired
    private ElasticsearchBaseDao elasticsearchBaseDao;
    
    private final static String index = SparrowIndex.SPA_FILES.getIndex();
    
    public String getDocId(String file_id)
    {
        return getFileMeta(file_id).getDoc_id();
    }
    
    public SpaFile getFileMeta(String file_id)
    {
        Map<String, Object> fileMeta =  elasticsearchBaseDao.getESDoc(index, file_id);
        if(fileMeta == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_FILE_ID, file_id);
        JSONObject jsonObject = new JSONObject(fileMeta);
        SpaFile spaFile = jsonObject.toJavaObject(SpaFile.class);
        return spaFile;
    }
}
