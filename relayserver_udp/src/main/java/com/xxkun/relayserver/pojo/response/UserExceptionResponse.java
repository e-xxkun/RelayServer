package com.xxkun.relayserver.pojo.response;

import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.ReplyResponseType;

import java.net.InetSocketAddress;
import java.util.List;

public class UserExceptionResponse extends Response{

    private ReplyResponseType type = ReplyResponseType.UNKNOWN;

    private List<UserInfo> userInfos;

    public UserExceptionResponse(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    public void setType(ReplyResponseType type) {
        this.type = type;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
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
        bodyBuffer.writeInt(userInfos.size());
        for (UserInfo info : userInfos) {
            bodyBuffer.writeLong(info.getUserId());
        }
    }
}
