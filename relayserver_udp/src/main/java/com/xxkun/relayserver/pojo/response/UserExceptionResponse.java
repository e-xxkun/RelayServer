package com.xxkun.relayserver.pojo.response;

import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.ResponseType;
import com.xxkun.udptransfer.TransferPacket;

import java.net.InetSocketAddress;
import java.util.List;

public class UserExceptionResponse extends Response{

    private ResponseType type = ResponseType.UNKNOWN;
    private List<UserInfo> userInfos;

    public UserExceptionResponse(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    public void setType(ResponseType type) {
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
        return userInfos.size() * Long.BYTES + Integer.BYTES * 2;
    }

    @Override
    public ResponseType getType() {
        return ResponseType.USER_EXCEPTION;
    }

    @Override
    protected void overwrite(TransferPacket.BodyBuffer bodyBuffer) {
        bodyBuffer.putInt(type.getCode());
        bodyBuffer.putInt(userInfos.size());
        for (UserInfo info : userInfos) {
            bodyBuffer.putLong(info.getUserId());
        }
    }
}
