package com.micerlab.sparrow.domain.pojo;

import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Deprecated
@Alias("resource")
public class Resource {

    private String resource_id;

    private String resource_name = "未命名";

    private String resource_type;

    private String creator_id;

    private Timestamp created_at;

    private String thumbnail;

    private int modifiable = 1;

    private int isRoot = 0;

    private int isHome = 0;

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getModifiable() {
        return modifiable;
    }

    public void setModifiable(int modifiable) {
        this.modifiable = modifiable;
    }

    public int getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(int isRoot) {
        this.isRoot = isRoot;
    }

    public int getIsHome() {
        return isHome;
    }

    public void setIsHome(int isHome) {
        this.isHome = isHome;
    }
}
