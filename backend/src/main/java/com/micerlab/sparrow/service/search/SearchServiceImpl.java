package com.micerlab.sparrow.service.search;

import com.micerlab.sparrow.dao.es.SearchDao;
import com.micerlab.sparrow.dao.es.SparrowIndex;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SearchRequestParams;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private SearchDao searchDao;
    
    public SearchServiceImpl(SearchDao searchDao)
    {
        this.searchDao = searchDao;
    }
    
    public Result getSearchSuggestions(String type, String keyword, int size)
    {
        List<String> suggestions =  searchDao.suggestions(type, keyword, size);
        return Result.OK().data(suggestions).build();
    }
    
    public Result getTopAssociations(String keyword, int catgory_count, int tag_count)
    {
        Map<String, Object> topAssociations = searchDao.topAssociations(keyword, catgory_count, tag_count);
        return Result.OK().data(topAssociations).build();
    }
    
    public Result getSearchResults(SearchRequestParams params)
    {
        Map<String, Object> data = searchDao.searchResults(params);
        return Result.OK().data(data).build();
    }
    
    public Result searchSpaFilters(SpaFilterType spaFilterType, String keyword, int size)
    {
        List<Map<String, Object>> spaFilters = searchDao.searchSpaFilters(spaFilterType, keyword, size);
        return Result.OK().data(spaFilters).build();
    }

    @Override
    public Result searchUser(String keyword, int size) {
        List<Map<String, Object>> data = searchDao.searchUserOrGroup(keyword, SparrowIndex.SPA_USER.getIndex(), size);
        return Result.OK().data(data).build();
    }

    @Override
    public Result searchGroup(String keyword, int size) {
        List<Map<String, Object>> data = searchDao.searchUserOrGroup(keyword, SparrowIndex.SPA_GROUP.getIndex(), size);
        return Result.OK().data(data).build();
    }
}
