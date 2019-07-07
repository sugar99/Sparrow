package com.micerlab.sparrow.service.search;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SpaFilterType;

import java.util.Map;

public interface SearchService
{
    Result getSearchSuggestions(String type, String keyword, int size);
    
    Result getTopAssociations(String keyword, int catgory_count, int tag_count);
    
    Result getSearchResults(Map<String, Object> searchResultParams);
    
    Result searchSpaFilterTypes(SpaFilterType spaFilterType, String keyword, int size);

    Result searchUser(String keyword);

    Result searchGroup(String keyword);
}
