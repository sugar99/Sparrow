package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.domain.doc.SpaDoc;
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
    
    public List<Map<String, Object>> getFiles(String doc_id)
    {
        List<Map<String, Object>> files = ESBaseDao.termsLookup(
                sparrowIndices.getFile(),
                index(),
                doc_id,
                "files"
        );
        for(Map<String, Object> file: files)
            file.put("resource_type", "file");
        return files;
    }
}
