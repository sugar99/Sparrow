package com.micerlab.sparrow.service;

import com.micerlab.sparrow.dao.es.SearchDao;
import com.micerlab.sparrow.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SearchService
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private SearchDao searchDao;
    
    public SearchService(SearchDao searchDao)
    {
        this.searchDao = searchDao;
    }
    
    public Result getSearchSuggestions(String type, String keyword, int size)
    {
        return null;
    }
    
    public Result getTopAssociations(String keyword, int catgory_count, int tag_count)
    {
        return null;
    }
    
    public Result getSearchResults()
    {
        return null;
    }
}
