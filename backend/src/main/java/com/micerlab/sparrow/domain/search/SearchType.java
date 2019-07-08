package com.micerlab.sparrow.domain.search;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;

public enum SearchType
{
    ALL(),
    IMAGE(),
    DOC(),
    VIDEO(),
    AUDIO(),
    OTHERS()
    ;
    
    private String type;
    
    SearchType()
    {
        this.type = name().toLowerCase();
    }
    
    public String getType()
    {
        return type;
    }
    
    @Override
    public String toString()
    {
        return type;
    }
    
    public static SearchType fromType(String type)
    {
        try
        {
            return SearchType.valueOf(type.toUpperCase());
        }catch (Exception ex)
        {
            throw new BusinessException(ErrorCode.PARAM_ERR_SEARCH_TYPE, "error search type <" + type + ">");
        }
    }
    
    public static void validateSearchType(String type)
    {
        fromType(type);
    }
}
