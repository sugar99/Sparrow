package com.micerlab.sparrow.domain.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyCount
{
    private String key;
    private long doc_count;
    
    public KeyCount(String key, long doc_count)
    {
        this.key = key;
        this.doc_count = doc_count;
    }
}
