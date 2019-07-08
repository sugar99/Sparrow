package com.micerlab.sparrow.domain.search;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Category
{
    private int id;
    private String title;
    private String desc;
    
    public Category(int id, String title)
    {
        this.id = id;
        this.title = title;
    }
    
    public Category(int id, String title, String desc)
    {
        this.id = id;
        this.title = title;
        this.desc = desc;
    }
}
