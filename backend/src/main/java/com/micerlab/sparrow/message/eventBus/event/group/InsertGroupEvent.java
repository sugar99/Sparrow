package com.micerlab.sparrow.message.eventBus.event.group;

import com.micerlab.sparrow.domain.pojo.Group;

public class InsertGroupEvent {
    public Group getGroup() {
        return group;
    }

    public InsertGroupEvent(Group group) {
        this.group = group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private Group group;


}
