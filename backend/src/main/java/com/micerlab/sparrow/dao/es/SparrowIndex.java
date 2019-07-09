package com.micerlab.sparrow.dao.es;

public enum SparrowIndex
{
    SPA_DOCS("spa_docs"),
    SPA_FILES("spa_files"),
    SPA_CATEGORIES("spa_categories"),
    SPA_TAGS("spa_tags"),
    SPA_USER("spa_user"),
    SPA_GROUP("spa_group")
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
