package com.micerlab.sparrow.domain.principal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnonymousPrincipal implements IPrincipal, Serializable {
    public static final String ANONYMOUS = "anonymous";

    public AnonymousPrincipal() {
        //NOP
    }
    @Override
    public String getName() {
        return ANONYMOUS;
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getUser_id() {
        return null;
    }

    @Override
    public String getWork_no() {
        return null;
    }

    @Override
    public boolean isGuest() {
        return false;
    }

    @Override
    public List<String> getGroupsIdList() {
        return new ArrayList<>();
    }
}
