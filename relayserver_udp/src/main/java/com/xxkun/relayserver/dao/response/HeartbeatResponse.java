package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.component.exception.ResponseConvertException;
import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.UserSession;

import java.net.InetSocketAddress;

public class HeartbeatResponse extends Response{
    private UserInfo userInfo;

    public HeartbeatResponse(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public int getBodyLength() {
        return userInfo.bytesLength();
    }

    @Override
    public ResponseType getType() {
        return ResponseType.REPLY;
    }

    @Override
    protected void overwriteToByteArray(BodyBuffer bodyBuffer) throws ResponseConvertException {
        bodyBuffer.writeString(userInfo.getSession().getToken());
    }
}
