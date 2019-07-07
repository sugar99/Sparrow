package com.micerlab.sparrow.eventBus.event.doc;

public class DeleteDocEvent {
    private final String user_id;

    private final String resource_id;

    public DeleteDocEvent(String user_id, String resource_id) {
        this.user_id = user_id;
        this.resource_id = resource_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getResource_id() {
        return resource_id;
    }
}
