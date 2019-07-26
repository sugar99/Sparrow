package com.micerlab.sparrow.message.eventBus.event.user;

public class InsertUserEvent {

    private String user_id;

    private String username;

    private String password;

    private String email;

    private String work_no;

    public InsertUserEvent(String user_id, String username, String work_no, String password, String email) {
        this.user_id = user_id;
        this.username = username;
        this.work_no = work_no;
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWork_no() {
        return work_no;
    }

    public void setWork_no(String work_no) {
        this.work_no = work_no;
    }
}
