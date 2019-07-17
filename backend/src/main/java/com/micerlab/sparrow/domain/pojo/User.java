package com.micerlab.sparrow.domain.pojo;

import org.apache.ibatis.type.Alias;

@Alias("user")
public class User {

    private String user_id;

    private String username;

    private String password;

    private String work_no;

    private String email;

    private int isAdmin;

    private String personal_dir;

    private String personal_group;

    public User(String user_id, String username, String password, String work_no, String email) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.work_no = work_no;
        this.email = email;
        this.isAdmin = 0;
    }

    public String getPersonal_group() {
        return personal_group;
    }

    public void setPersonal_group(String personal_group) {
        this.personal_group = personal_group;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWork_no() {
        return work_no;
    }

    public void setWork_no(String work_no) {
        this.work_no = work_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPersonal_dir() {
        return personal_dir;
    }

    public void setPersonal_dir(String personal_dir) {
        this.personal_dir = personal_dir;
    }
}
