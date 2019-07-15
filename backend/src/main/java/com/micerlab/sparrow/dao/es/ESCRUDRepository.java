package com.micerlab.sparrow.dao.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class ESCRUDRepository<E>
{
    protected String index;
    
    @Autowired
    protected ElasticsearchBaseDao elasticsearchBaseDao;
    
    public ESCRUDRepository(String index)
    {
        this.index = index;
    }
    
    public E get(String id)
    {
        return null;
    }
    
    
}
