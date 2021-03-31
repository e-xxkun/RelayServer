package com.xxkun.relayserver_udp.dao;

import com.xxkun.relayserver_udp.component.exception.MessageResolutionException;
import com.xxkun.relayserver_udp.dto.IMessageType;
import com.xxkun.relayserver_udp.dto.MessageType;
import org.springframework.lang.NonNull;

public abstract class Message {

    public static final int MESSAGE_TOKEN_LEN = 16;

    private final UDPField udpField;

    public Message(@NonNull UDPField udpField) throws MessageResolutionException {
        this.udpField = udpField;
        decode(udpField);
    }

    public UDPField convertToUDPField() {
        overwriteToUDPField(udpField);
        return udpField;
    }

    public abstract void overwriteToUDPField(UDPField udpField);

    public String getToken() {
        return null;
    }

    public abstract MessageType getType();

    protected abstract void decode(UDPField udpField) throws MessageResolutionException;

    public static Message decodeFromUDPField(@NonNull UDPField udpField) {
        UDPField.BodyBuffer buffer = udpField.getByteBuffer();
        int type = buffer.getInt();
        IMessageType messageType = IMessageType.fromTypeCode(type);
        if (messageType == null)
            return null;
        Message message = null;
        try {
            message = messageType.createMessage(udpField);
        } catch (MessageResolutionException e) {
            e.printStackTrace();
        }
        return message;
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

}
