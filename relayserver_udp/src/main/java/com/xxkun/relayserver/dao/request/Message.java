package com.xxkun.relayserver.dao.request;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.dto.IMessageType;
import com.xxkun.relayserver.dto.MessageType;
import org.springframework.lang.NonNull;

public abstract class Message {

    public static final int MESSAGE_TOKEN_LEN = 16;

    private final Request request;

    public Message(@NonNull Request request) throws MessageResolutionException {
        this.request = request;
        decode(request);
    }

    public String getToken() {
        return null;
    }

    public abstract MessageType getType();

    protected abstract void decode(Request request) throws MessageResolutionException;

    public static Message decodeFromRequest(@NonNull Request request) {
        Request.BodyBuffer buffer = request.getBodyBuffer();
        int type = buffer.getInt();
        IMessageType messageType = IMessageType.fromTypeCode(type);
        if (messageType == null)
            return null;
        Message message = null;
        try {
            message = messageType.createMessage(request);
        } catch (MessageResolutionException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return request.equals(message.request);
    }

    @Override
    public int hashCode() {
        return request.hashCode();
    }

}
