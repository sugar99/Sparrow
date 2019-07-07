package com.micerlab.sparrow.eventBus.event.doc;

public class UpdateDocEvent {
    private final String resource_id;

    private final String resource_name;

    public UpdateDocEvent(String resource_id, String resource_name) {
        this.resource_id = resource_id;
        this.resource_name = resource_name;
    }

    public String getResource_id() {
        return resource_id;
    }

    public String getResource_name() {
        return resource_name;
    }
}
