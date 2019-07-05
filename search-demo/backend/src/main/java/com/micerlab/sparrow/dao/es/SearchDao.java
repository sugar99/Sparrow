package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SearchDao
{
    private static Logger logger = LoggerFactory.getLogger(SearchDao.class);
    
    private SearchSuggestionDao searchSuggestionDao;
    
    public SearchDao(SearchSuggestionDao searchSuggestionDao)
    {
        this.searchSuggestionDao = searchSuggestionDao;
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
}
