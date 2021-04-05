package com.xxkun.relayserver.dao;

public class FriendInfo {

    private long userId;
    private String name;
    private String url;

    public FriendInfo(long friendIp) {
        this.userId = friendIp;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUrl() {
        return name + "@" + url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBytesLength() {
        return Long.BYTES + name.length() * Character.BYTES + url.length() * Character.BYTES + Character.BYTES;
    }
}
