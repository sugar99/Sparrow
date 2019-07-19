package com.micerlab.sparrow.domain.pojo;

import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("directory")
public class Directory {
    private String id;
    private String title;
    private String thumbnail;
    private int root;
    private int home;
    private int personal;
    private int modifiable;
    private String creator_id;
    private Timestamp created_at;

    public Directory(String id, String creator_id, Timestamp created_at) {
        this.id = id;
        this.title = "未命名";
        this.thumbnail = "./assets/images/docCnt.png";
        this.creator_id = creator_id;
        this.created_at = created_at;
        this.root = 0;
        this.home = 0;
        this.personal = 0;
        this.modifiable = 1;
    }

    public Directory(String id, String creator_id, Timestamp created_at, String title) {
        this(id, creator_id, created_at);
        this.setTitle(title);
    }

    public Directory(String id, String title, String thumbnail, int root, int home, int personal,
                     int modifiable, String creator_id, Timestamp created_at) {
        this(id, creator_id, created_at);
        this.title =title;
        this.thumbnail = thumbnail;
        this.root = root;
        this.home = home;
        this.personal = personal;
        this.modifiable = modifiable;
    }

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
