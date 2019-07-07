package com.micerlab.sparrow.eventBus.event.doc;

import java.sql.Timestamp;

public class InsertDocEvent {
    private final String resource_id;

    private final String resource_type;

    private final String creator_id;

    private final Timestamp created_at;

    public InsertDocEvent(String resource_id, String resource_type, String creator_id, Timestamp created_at) {
        this.resource_id = resource_id;
        this.resource_type = resource_type;
        this.creator_id = creator_id;
        this.created_at = created_at;
    }

    public String getResource_id() {
        return resource_id;
    }

    public String getResource_type() {
        return resource_type;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }
}
