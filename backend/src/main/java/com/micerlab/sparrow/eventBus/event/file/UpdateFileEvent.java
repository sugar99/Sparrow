package com.micerlab.sparrow.eventBus.event.file;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateFileEvent
{
    private String id;
    private JSONObject jsonMap;
    
    public UpdateFileEvent(String id, JSONObject jsonMap)
    {
        this.id = id;
        this.jsonMap = jsonMap;
    }
}
