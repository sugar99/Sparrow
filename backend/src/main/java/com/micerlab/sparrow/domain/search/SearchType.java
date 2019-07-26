package com.micerlab.sparrow.domain.search;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.meta.FileType;
import com.micerlab.sparrow.utils.BusinessException;

public enum SearchType
{
    ALL(null),
    IMAGE(FileType.IMAGE),
    DOC(FileType.DOC),
    VIDEO(FileType.VIDEO),
    AUDIO(FileType.AUDIO),
    OTHERS(FileType.OTHERS),
    DOC_CONTENT(FileType.DOC) // 文档全文检索
    ;
    
    private final String type;
    private final FileType fileType;
    
    SearchType(FileType fileType)
    {
        this.fileType = fileType;
        this.type = name().toLowerCase();
    }
    
    public String getType()
    {
        return type;
    }
    
    public FileType getFileType()
    {
        return fileType;
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
