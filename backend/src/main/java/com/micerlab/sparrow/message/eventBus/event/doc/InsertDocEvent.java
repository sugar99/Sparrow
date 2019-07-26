package com.micerlab.sparrow.message.eventBus.event.doc;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class InsertDocEvent {
    private final String resource_id;

    private final String resource_type;

    private final String creator;

    private final Timestamp created_time;

    private final Timestamp modified_time;

    private final String title;

    private final String desc;

    private final List<String> files;

    private final byte meta_state;

    public InsertDocEvent(String resource_id, String resource_type, String creator, Timestamp created_time,
                          Timestamp modified_time) {
        this.resource_id = resource_id;
        this.resource_type = resource_type;
        this.creator = creator;
        this.created_time = created_time;
        this.modified_time = modified_time;
        this.title = "未命名";
        this.desc = "";
        this.files = Collections.emptyList();
        this.meta_state = 0;
    }

    public String getResource_id() {
        return resource_id;
    }

    public String getResource_type() {
        return resource_type;
    }

    public String getCreator() {
        return creator;
    }

    public Timestamp getCreated_time() {
        return created_time;
    }

    public Timestamp getModified_time() {
        return modified_time;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
    
    public List<String> getFiles()
    {
        return files;
    }
    
    public byte getMeta_state() {
        return meta_state;
    }

}
