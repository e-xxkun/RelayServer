package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.dao.FriendInfo;
import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dto.ReplyResponseType;

import java.net.InetSocketAddress;
import java.util.List;

public class FriendExceptionResponse extends Response{

    private ReplyResponseType type = ReplyResponseType.UNKNOWN;

    private List<FriendInfo> friendInfos;

    public FriendExceptionResponse(InetSocketAddress socketAddress, long sequence) {
        super(socketAddress);
        setSequence(sequence);
    }

    public void setType(ReplyResponseType type) {
        this.type = type;
    }

    public List<FriendInfo> getFriendInfos() {
        return friendInfos;
    }

    public void setFriendInfos(List<FriendInfo> friendInfos) {
        this.friendInfos = friendInfos;
    }

    @Override
    public int getBodyLength() {
        return type.length();
    }

    @Override
    public ResponseType getType() {
        return ResponseType.REPLY;
    }

    @Override
    protected void overwriteToByteArray(BodyBuffer bodyBuffer) {
        bodyBuffer.position(0);
        bodyBuffer.writeInt(type.getCode());
        bodyBuffer.writeInt(friendInfos.size());
        for (FriendInfo info : friendInfos) {
            bodyBuffer.writeLong(info.getUserId());
        }
    }
}
