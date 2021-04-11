package com.xxkun.relayserver.dao.mbg.model;

import com.xxkun.relayserver.dto.UserStatus;

public class User {
    private Long id;

    private String name;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getState() {
        return UserStatus.ONLINE;
    }
}