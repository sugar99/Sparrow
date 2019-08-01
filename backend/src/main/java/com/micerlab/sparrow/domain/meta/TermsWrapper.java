package com.micerlab.sparrow.domain.meta;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * ES terms look up helper class
 */
@Setter
@Getter
public class TermsWrapper
{
    private long total;
    private List<Map<String, Object>> items;
    
    public TermsWrapper()
    {
    }
    
    public TermsWrapper(long total, List<Map<String, Object>> items)
    {
        this.total = total;
        this.items = items;
    }
}
