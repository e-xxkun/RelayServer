package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.component.exception.ResponseConvertException;
import com.xxkun.relayserver.dao.UserInfo;

import java.net.InetSocketAddress;
import java.util.List;

public class PunchResponse extends Response {

    private List<UserInfo> userInfos;

    private int index = 0;

    private int bodyLength;

    public PunchResponse(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    @Override
    public boolean hashNext() {
        return index < userInfos.size();
    }

    @Override
    public Response next() {
        if (!hashNext()) {
            return null;
        }
        PunchResponse response = new PunchResponse(getSocketAddress());
        response.userInfos = userInfos;
        response.index = index;
        return super.next();
    }

    @Override
    public int getBodyLength() {
        return bodyLength;
    }

    @Override
    public ResponseType getType() {
        return ResponseType.PUNCH;
    }

    @Override
    protected void overwriteToByteArray(BodyBuffer bodyBuffer) throws ResponseConvertException {
        bodyLength = 0;
        bodyBuffer.skip(Integer.BYTES);
        int i = index;
        for (;i < userInfos.size() && bodyLength < bodyBuffer.limit();i ++) {
            UserInfo info = userInfos.get(i);
            bodyBuffer.writeLong(info.getUserId());
            bodyBuffer.writeString(info.getNameUrl());
            bodyLength += info.bytesLength();
        }
        bodyBuffer.position(0);
        bodyBuffer.writeInt(i - index);
        index = i;
    }
}
