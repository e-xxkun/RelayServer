package com.xxkun.relayserver_udp.dao.message;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dto.MessageType;

public class HeartbeatMessage extends Message {

    private String token;

    public HeartbeatMessage(UDPField udpField) {
        super(udpField);
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public UDPField convertToUDPField() {
        return null;
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
    protected void decode(UDPField udpField) {
        UDPField.BodyBuffer buffer = udpField.getByteBuffer();

        // skip the message type byte
        buffer.skip(Integer.BYTES);

        token = buffer.getString(MESSAGE_TOKEN_LEN);
    }
}
