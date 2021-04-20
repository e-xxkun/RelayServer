package com.xxkun.relayserver.pojo.request.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.pojo.IMessageType;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.udptransfer.TransferPacket;

import java.nio.BufferUnderflowException;

public class PunchMessage extends Message {

    private UserInfo[] userInfos;

    public PunchMessage(Request udpField) throws MessageResolutionException {
        super(udpField);
    }

    public UserInfo[] getUserInfos() {
        return userInfos;
    }

    @Override
    public IMessageType getType() {
        return MessageType.GET.PUNCH;
    }

    @Override
    protected void decode(Request udpField) throws MessageResolutionException {
        TransferPacket.BodyBuffer buffer = udpField.getBodyBuffer();
        try {
            buffer.position(getHeadLength());
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
