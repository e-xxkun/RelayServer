package com.xxkun.relayserver.dao.request.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.dao.FriendInfo;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.Request;
import com.xxkun.relayserver.dto.MessageType;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;

public class PunchMessage extends Message {

    private String token;

    private FriendInfo[] friendInfos;

    public PunchMessage(Request udpField) throws MessageResolutionException {
        super(udpField);
    }

    public FriendInfo[] getFriendInfos() {
        return friendInfos;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public MessageType getType() {
        return MessageType.PUNCH;
    }

    @Override
    protected void decode(Request udpField) throws MessageResolutionException {
        Request.BodyBuffer buffer = udpField.getBodyBuffer();
        try {
            // skip the message type byte
            buffer.skip(Integer.BYTES);
            token = buffer.getString(MESSAGE_TOKEN_LEN);
            int count = buffer.getInt();
            friendInfos = new FriendInfo[count];

            // count|friend_id (int)|(long) 2|0~9223372036854775807
            for (int i = 0;i < count;i ++) {
                long friendIp = buffer.getLong();
                friendInfos[i] = new FriendInfo(friendIp);
            }
        } catch (BufferUnderflowException | IllegalArgumentException e) {
            throw new MessageResolutionException();
        }
    }
}
