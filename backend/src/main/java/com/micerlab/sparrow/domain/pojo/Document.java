package com.micerlab.sparrow.domain.pojo;

import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;

@Alias("document")
public class Document {
    private String id;
    private String title;
    private String thumbnail;
    private String creator_id;
    private Timestamp created_at;

    public Document(String id, String creator_id, Timestamp created_at) {
        this.id = id;
        this.creator_id = creator_id;
        this.created_at = created_at;
        this.title = "未命名";
        this.thumbnail = "./assets/images/doc.png";
    }

    public Document(String id, String title, String thumbnail, String creator_id, Timestamp created_at) {
        this(id, creator_id, created_at);
        this.title = title;
        this.thumbnail = thumbnail;
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
