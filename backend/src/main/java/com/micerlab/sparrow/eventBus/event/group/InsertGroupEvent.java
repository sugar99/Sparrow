package com.micerlab.sparrow.eventBus.event.group;

import java.sql.Timestamp;

public class InsertGroupEvent {
    private String group_id;
    private String group_name;
    private String creator;
    private Timestamp created_at;

    public InsertGroupEvent(String group_id, String group_name, String creator, Timestamp created_at) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.creator = creator;
        this.created_at = created_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
