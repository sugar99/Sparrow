package com.micerlab.sparrow.domain.meta;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.utils.BusinessException;

public enum SpaFilterType
{
    TAG("tag", "tags"),
    CATEGORY("category", "categories");
    
    private String type;
    private String types;
    
    SpaFilterType(String type, String types)
    {
        this.type = type;
        this.types = types;
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getTypes()
    {
        return types;
    }
    
    @Override
    public String toString()
    {
        return type;
    }
    
    public static SpaFilterType fromType(String type)
    {
        try{
            return SpaFilterType.valueOf(type);
        }catch (Exception ex)
        {
            throw new BusinessException(ErrorCode.PARAM_ERR_FILTER_TYPE, type);
        }
    }
    
    public static SpaFilterType fromTypes(String types)
    {
        if(SpaFilterType.TAG.types.equals(types))
            return SpaFilterType.TAG;
        else if(SpaFilterType.CATEGORY.types.equals(types))
            return SpaFilterType.CATEGORY;
        throw new BusinessException(ErrorCode.PARAM_ERR_FILTER_TYPES, types);
    }
    
    
}
