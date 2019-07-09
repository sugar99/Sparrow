package com.micerlab.sparrow.domain.params;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class SpaDocUpdateParams
{
    private String title;
    private String desc;
    
    public Map<String, Object> toMap()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("desc", desc);
        return map;
    }
}
