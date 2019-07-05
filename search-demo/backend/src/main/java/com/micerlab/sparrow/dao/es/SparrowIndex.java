package com.micerlab.sparrow.dao.es;

public enum SparrowIndex
{
    SPA_DOCS("spa_docs"),
    SPA_FILES("spa_files"),
    SPA_CATEGORIES("spa_categories"),
    SPA_TAGS("spa_tags"),
    
    ;
    
    private String index;
    
    SparrowIndex(String index)
    {
        this.index = index;
    }
    
    public String getIndex()
    {
        return index;
    }
}
