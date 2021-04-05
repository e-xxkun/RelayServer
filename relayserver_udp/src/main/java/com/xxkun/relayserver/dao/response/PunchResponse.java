package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.component.exception.ResponseConvertException;
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
        return friendInfo.getBytesLength();
    }

    @Override
    public ResponseType getType() {
        return ResponseType.PUNCH;
    }

    @Override
    protected void overwriteToByteArray(BodyBuffer bodyBuffer) throws ResponseConvertException {
        if (friendInfo == null || friendInfo.getBytesLength() > bodyBuffer.limit()) {
            throw new ResponseConvertException();
        }
        bodyBuffer.writeLong(friendInfo.getUserId());
        bodyBuffer.writeString(friendInfo.getNameUrl());
    }
}
