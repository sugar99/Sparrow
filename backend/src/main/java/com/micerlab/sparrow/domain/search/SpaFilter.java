package com.micerlab.sparrow.domain.search;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpaFilter
{
    private int id;
    private String title;
    private String desc;
    
    public SpaFilter()
    {
    }
    
    public SpaFilter(int id, String title)
    {
        this.id = id;
        this.title = title;
    }
    
    public SpaFilter(int id, String title, String desc)
    {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }
}
