package com.xxkun.relayserver.dao.request.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.Request;
import com.xxkun.relayserver.dto.MessageType;

public class HeartbeatMessage extends Message {

    private String token;

    public HeartbeatMessage(Request udpField) throws MessageResolutionException {
        super(udpField);
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public MessageType getType() {
        return MessageType.HEARTBEAT;
    }

    @Override
    protected void decode(Request udpField) {
        Request.BodyBuffer buffer = udpField.getByteBuffer();

        // skip the message type byte
        buffer.skip(Integer.BYTES);
        token = buffer.getString(MESSAGE_TOKEN_LEN);
    }
}
