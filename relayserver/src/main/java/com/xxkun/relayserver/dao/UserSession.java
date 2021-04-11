package com.xxkun.relayserver.dao;

public class UserSession {

    private long userId;
    private String token;

    public UserSession(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public int bytesLength() {
        return token.length() * Character.BYTES;
    }
}
