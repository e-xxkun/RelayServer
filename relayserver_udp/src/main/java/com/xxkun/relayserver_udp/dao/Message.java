package com.xxkun.relayserver_udp.dao;

import com.xxkun.relayserver_udp.dto.MessageType;
import reactor.util.annotation.NonNull;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public abstract class Message {

    public static final int UDP_MSG_IN_BUFF_LEN = 512;
    
    private final UDPField udpField;

    public Message(@NonNull UDPField udpField) {
        this.udpField = udpField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return udpField.equals(message.udpField);
    }

    @Override
    public int hashCode() {
        return udpField.hashCode();
    }

    public abstract UDPField convertToUDPField();

    public abstract String getToken();

    public abstract MessageType getType();

    public static Message decodeFromUDPField(UDPField udpField) {
        if (udpField == null)
            return null;
        ByteBuffer buffer = udpField.getByteBuffer();
        int type = buffer.getInt();
        return MessageType.fromTypeValue(type).createMessage(udpField);
    }
}
