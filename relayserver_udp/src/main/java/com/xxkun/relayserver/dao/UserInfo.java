package com.xxkun.relayserver.dao;

public class UserInfo {
    private long userId;

    public UserInfo(long userId) {
        this.userId = userId;
    }

    public boolean isOffload() {
        return false;
    }

    public long getUserId() {
        return userId;
    }

    public String getNameUrl() {
        return null;
    }

    public int getBytesLength() {
        return 0;
    }
}
