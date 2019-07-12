package com.micerlab.sparrow.eventBus.event.doc;

import java.sql.Timestamp;

public class UpdateDocEvent {
    private final String id;

    private final String title;

    private final String desc;

    private final Timestamp modified_time;

    private final byte meta_state;

    public UpdateDocEvent(String id, String title, String desc, Timestamp modified_time) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.modified_time = modified_time;
        meta_state = 1;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public Timestamp getModified_time() {
        return modified_time;
    }

    public byte getMeta_state() {
        return meta_state;
    }

}
