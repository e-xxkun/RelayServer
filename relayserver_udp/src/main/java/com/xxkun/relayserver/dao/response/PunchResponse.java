package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.dao.FriendInfo;

import java.net.InetSocketAddress;

public class PunchResponse extends Response {

    private FriendInfo friendInfo;

    public PunchResponse(InetSocketAddress socketAddress, FriendInfo friendInfo) {
        super(socketAddress);
        setFriendInfo(friendInfo);
    }

    public FriendInfo getFriendInfo() {
        return friendInfo;
    }

    public void setFriendInfo(FriendInfo friendInfo) {
        this.friendInfo = friendInfo;
    }

    @Override
    public int getBodyLength() {
        return 0;
    }

    @Override
    public ResponseType getType() {
        return ResponseType.PUNCH;
    }

    @Override
    protected void overwriteToByteArray(BodyBuffer bodyBuffer) {

    }
}
