package com.xxkun.relayserver.pojo.request.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.relayserver.pojo.MessageType;

import java.nio.BufferUnderflowException;

public class PunchMessage extends Message {

    private String token;

    private UserInfo[] userInfos;

    public PunchMessage(Request udpField) throws MessageResolutionException {
        super(udpField);
    }

    public UserInfo[] getUserInfos() {
        return userInfos;
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
            userInfos = new UserInfo[count];

            // count|user_id (int)|(long) 2|0~9223372036854775807
            for (int i = 0;i < count;i ++) {
                long userIp = buffer.getLong();
                userInfos[i] = new UserInfo(userIp);
            }
        } catch (BufferUnderflowException | IllegalArgumentException e) {
            throw new MessageResolutionException();
        }
    }
}
