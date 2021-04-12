package com.xxkun.relayserver.dao.mbg.model;

import com.xxkun.relayserver.dto.UserStatus;

public class User {

    private Integer incId;

    private Long userId;

    private String id;

    private String name;

    private String password;

    private UserStatus state;

    public Integer getIncId() {
        return incId;
    }

    public void setIncId(Integer incId) {
        this.incId = incId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatus getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = UserStatus.fromCode(state);
    }
}