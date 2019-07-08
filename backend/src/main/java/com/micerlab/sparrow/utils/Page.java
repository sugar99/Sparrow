package com.micerlab.sparrow.utils;

import lombok.Getter;

@Getter
public class Page
{
    private int page;
    private int per_page;
    
    private int from;
    private int size;
    
    public Page(int page, int per_page)
    {
        this.page = page;
        this.per_page = per_page;
        
        from = (page - 1) * per_page;
        size = per_page;
    }
    
    public static Page fromAndSize(int from, int size)
    {
        int per_page = size;
        int page = from / per_page + 1;
        return new Page(page, per_page);
    }
    
    
}
