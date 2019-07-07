package com.micerlab.sparrow.domain;

public enum  ActionType {

    READ("100"),
    WRITE("010"),
    //暂时没用
    EXECUTE("001");

    private String actionCode;

    ActionType(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionCode() {
        return actionCode;
    }
}
