package com.micerlab.sparrow.domain.pojo;

import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("directory")
public class Directory {
    private String id;
    private String title = "未命名";
    private String thumbnail = "./assets/images/docCnt.png";
    private int root = 0;
    private int home = 0;
    private int personal = 0;
    private int modifiable = 1;
    private String creator_id;
    private Timestamp created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public int getModifiable() {
        return modifiable;
    }

    public void setModifiable(int modifiable) {
        this.modifiable = modifiable;
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
