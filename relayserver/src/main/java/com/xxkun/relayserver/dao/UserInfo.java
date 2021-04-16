package com.xxkun.relayserver.dao;

import com.xxkun.relayserver.pojo.NatType;
import com.xxkun.relayserver.pojo.UserStatus;

import java.net.InetSocketAddress;

public class UserInfo {

    private final long userId;

    private String name;

    private UserStatus status;

    private NatType natType;

    private UserSession session;

    private String url;

    private UserIdentifier identifier;

    public UserInfo(long userId) {
        this.userId = userId;
    }

    public NatType getNatType() {
        return natType;
    }

    public void setNatType(NatType natType) {
        this.natType = natType;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserSession getSession() {
        return session;
    }

    public UserIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UserIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public boolean isOffload() {
        return false;
    }

    public long getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameUrl() {
        return null;
    }

    public InetSocketAddress getSocketAddress() {
        return null;
    }

    public int bytesLength() {
        return 0;
    }
}
