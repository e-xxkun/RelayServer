package com.xxkun.relayserver.dao.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.dao.Message;
import com.xxkun.relayserver.dao.Request;
import com.xxkun.relayserver.dto.MessageType;

import java.net.InetSocketAddress;

public class PunchMessage extends Message {

    private String token;

    private InetSocketAddress peer;

    public PunchMessage(Request udpField) throws MessageResolutionException {
        super(udpField);
    }

    public InetSocketAddress getPeer() {
        return peer;
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
        Request.BodyBuffer buffer = udpField.getByteBuffer();

        // skip the message type byte
        if (!buffer.skip(Integer.BYTES)) {
            throw new MessageResolutionException();
        }
        token = buffer.getString(MESSAGE_TOKEN_LEN);
        // ip_length|ip|port (int)|(char[])|(int) 15|225.225.225.225|8080
        int ipLength = buffer.getInt();
        if (ipLength < buffer.remainLength() - Integer.BYTES) {
            peer = new InetSocketAddress(buffer.getString(ipLength), buffer.getInt());
        } else {
            throw new MessageResolutionException();
        }
    }
}
