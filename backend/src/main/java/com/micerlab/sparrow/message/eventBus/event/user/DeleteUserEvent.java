package com.micerlab.sparrow.message.eventBus.event.user;

public class DeleteUserEvent {
    private String user_id;

    public DeleteUserEvent(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
