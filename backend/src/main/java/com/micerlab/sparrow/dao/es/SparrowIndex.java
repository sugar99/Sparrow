package com.micerlab.sparrow.dao.es;

public enum SparrowIndex
{
    SPA_DOCS,
    SPA_FILES,
    SPA_CATEGORIES,
    SPA_TAGS,
    SPA_USER,
    SPA_GROUP
    ;
    
    private String index;
    
//    SparrowIndex(String index) { this.index = index;  }
    
    SparrowIndex()
    {
        this.index = this.name().toLowerCase();
    }
    
    public String getIndex()
    {
        return index;
    }
}
