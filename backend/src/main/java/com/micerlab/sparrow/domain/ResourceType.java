package com.micerlab.sparrow.domain;

public enum  ResourceType {

    DIR("dir"),
    DOC("doc");

    private String type;

    ResourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
