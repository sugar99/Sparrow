package com.micerlab.sparrow.dao.es;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.domain.meta.SpaDoc;
import com.micerlab.sparrow.domain.meta.TermsWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class SpaDocDao extends ESCRUDRepository<SpaDoc>
{
    public SpaDocDao(ESBaseDao ESBaseDao, ESConfig.Indices indices)
    {
        super(SpaDoc.class, ESBaseDao, indices);
    }
    
    @Override
    protected String index()
    {
        return sparrowIndices.getDoc();
    }
    
    private Logger logger = LoggerFactory.getLogger(SpaDocDao.class);
    
    public JSONObject getFiles2(String doc_id, int page, int per_page)
    {
        TermsWrapper termsWrapper = ESBaseDao.termsLookup(
                sparrowIndices.getFile(),
                index(),
                doc_id,
                "files",
                page,
                per_page
        );
        JSONObject data = new JSONObject();
        data.put("total", termsWrapper.getTotal());
        data.put("files", termsWrapper.getItems());
        return data;
    }
    
    @Deprecated
    public List<Map<String, Object>> getFiles1(String doc_id, int page, int per_page)
    {
        List<Map<String, Object>> files = ESBaseDao.termsLookup(
                sparrowIndices.getFile(),
                index(),
                doc_id,
                "files",
                page,
                per_page
        ).getItems();
        for(Map<String, Object> file: files)
            file.put("resource_type", "file");
        return files;
    }
}
