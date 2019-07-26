package com.micerlab.sparrow.message.eventBus.event.doc;

public class DeleteDocEvent {
    private final String resource_id;

    public DeleteDocEvent(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_id() {
        return resource_id;
    }
}
