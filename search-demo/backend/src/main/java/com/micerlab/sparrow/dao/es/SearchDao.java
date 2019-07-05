package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class SearchDao
{
    private static Logger logger = LoggerFactory.getLogger(SearchDao.class);
    
    private SearchSuggestionDao searchSuggestionDao;
    
    private SearchAssociationDao searchAssociationDao;
    
    public SearchDao(SearchSuggestionDao searchSuggestionDao, SearchAssociationDao searchAssociationDao)
    {
        this.searchSuggestionDao = searchSuggestionDao;
        this.searchAssociationDao = searchAssociationDao;
    }
    
    public List<String> suggestions(String type, String keyword, int size)
    {
        try
        {
            return searchSuggestionDao.suggestions(type, keyword, size);
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERROR_ELASTICSEARCH, ex.getMessage());
        }
    }
    
    public Map<String, Object> topAssociations(String keyword, int category_count, int tag_count)
    {
        try
        {
            return searchAssociationDao.topAssociations(keyword, category_count, tag_count);
        } catch (IOException ex)
        {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            throw new BusinessException(ErrorCode.SERVER_ERROR_ELASTICSEARCH, ex.getMessage());
        }
    }
}
