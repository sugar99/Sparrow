package com.micerlab.sparrow.domain;

import com.micerlab.sparrow.utils.BusinessException;

public enum SearchType
{
    ALL("all"),
    IMAGE("image"),
    DOC("doc"),
    VIDEO("video"),
    AUDIO("audio"),
    OTHERS("others")
    ;
    
    private String type;
    
    SearchType(String type)
    {
        this.type = type;
    }
    
    public String getType()
    {
        return type;
    }
    
    public static void validateSearchType(String type)
    {
        try
        {
            SearchType searchType = SearchType.valueOf(type.toUpperCase());
        }catch (Exception ex)
        {
            throw new BusinessException(ErrorCode.PARAM_ERR_SEARCH_TYPE, "error search type <" + type + ">");
        }
    }
}
