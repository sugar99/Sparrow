package com.micerlab.sparrow.domain.pojo;

import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("group")
public class Group {

    private String group_id;

    private String group_name;

    private String group_desc;

    private String creator_id;

    private Timestamp created_at;

    private int personal = 0;

    public Group(String group_id, String group_name, String creator_id, Timestamp created_at, String group_desc,
                 int personal) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_desc = group_desc;
        this.creator_id = creator_id;
        this.created_at = created_at;
        this.personal = personal;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
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

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
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
}
