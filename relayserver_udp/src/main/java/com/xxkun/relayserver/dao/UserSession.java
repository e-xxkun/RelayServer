package com.xxkun.relayserver.dao;

public class UserSession {

    private long userId;
    private String token;

    public String getToken() {
        return token;
    }

    public int bytesLength() {
        return token.length() * Character.BYTES;
    }
}
