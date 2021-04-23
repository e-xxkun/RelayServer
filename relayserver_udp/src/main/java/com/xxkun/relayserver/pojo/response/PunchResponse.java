package com.xxkun.relayserver.pojo.response;

import com.xxkun.relayserver.pojo.ResponseType;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.udptransfer.TransferPacket;

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
    protected void overwrite(TransferPacket.BodyBuffer bodyBuffer) {
//        count|[user_id|len|name_url]   int|[long|byte|char[len]]
        bodyBuffer.position(getHeadLength() + Integer.BYTES);
        bodyLength = Integer.BYTES;
        int i = index;
        for (;i < userInfos.size();i ++) {
            UserInfo info = userInfos.get(i);
            String nameUrl = info.getNameUrl();
            int len = Long.BYTES + Integer.BYTES + nameUrl.length() * Character.BYTES;
            if (bodyLength + len > bodyBuffer.getBodyLength() - getHeadLength()) {
                break;
            }
            bodyBuffer.putLong(info.getUserId());
            bodyBuffer.put((byte) (nameUrl.length() & 0xff));
            bodyBuffer.putString(nameUrl);
            bodyLength += len;
        }
        bodyBuffer.position(getHeadLength());
        bodyBuffer.putInt(i - index);
        index = i;
    }
}
