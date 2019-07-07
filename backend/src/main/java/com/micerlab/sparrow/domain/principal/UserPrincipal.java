package com.micerlab.sparrow.domain.principal;

import java.io.Serializable;
import java.util.List;

public class UserPrincipal implements IPrincipal, Serializable {

    private String user_id;

    private String name;

    private String email;

    private String work_no;

    private List<String> groupsIdList;

    public UserPrincipal(String user_id, String name, String email, String work_no) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.work_no = work_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getWork_no() {
        return work_no;
    }

    public void setWork_no(String work_no) {
        this.work_no = work_no;
    }

    @Override
    public List<String> getGroupsIdList() {
        return groupsIdList;
    }

    public void setGroupsIdList(List<String> groupsIdList) {
        this.groupsIdList = groupsIdList;
    }

    @Override
    public boolean isGuest() {
        return true;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }
}
