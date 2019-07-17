package com.micerlab.sparrow.service.search;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SearchResultParams;
import com.micerlab.sparrow.domain.search.SpaFilterType;

public interface SearchService
{
    Result getSearchSuggestions(String type, String keyword, int size);
    
    Result getTopAssociations(String keyword, int catgory_count, int tag_count);
    
    Result getSearchResults(SearchResultParams params);
    
    Result searchSpaFilters(SpaFilterType spaFilterType, String keyword, int size);

    Result searchUser(String keyword, int size);

    Result searchGroup(String keyword, int size);
}
