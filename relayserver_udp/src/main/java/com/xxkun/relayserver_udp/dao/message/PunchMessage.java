package com.xxkun.relayserver_udp.dao.message;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dto.MessageType;

import java.net.InetSocketAddress;

public class PunchMessage extends Message {

    private String token;

    private InetSocketAddress peer;

    public PunchMessage(UDPField udpField) {
        super(udpField);
    }

    public InetSocketAddress getPeer() {
        return peer;
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
        return MessageType.PUNCH;
    }

    @Override
    protected void decode(UDPField udpField) {
        UDPField.BodyBuffer buffer = udpField.getByteBuffer();

        // skip the message type byte
        if (!buffer.skip(Integer.BYTES)) {
            return;
        }
        token = buffer.getString(MESSAGE_TOKEN_LEN);
        // ip_length|ip|port (int)|(char[])|(int) 15|225.225.225.225|8080
        int ipLength = buffer.getInt();
        if (ipLength < buffer.getRemainLength() - Integer.BYTES) {
            peer = new InetSocketAddress(buffer.getString(ipLength), buffer.getInt());
        }
    }
}
