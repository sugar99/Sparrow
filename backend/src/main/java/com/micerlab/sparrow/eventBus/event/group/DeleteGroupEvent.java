package com.micerlab.sparrow.eventBus.event.group;

public class DeleteGroupEvent {
    private String group_id;

    public DeleteGroupEvent(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
