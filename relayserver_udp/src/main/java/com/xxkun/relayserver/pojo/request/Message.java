package com.xxkun.relayserver.pojo.request;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.pojo.user.UserSession;
import com.xxkun.relayserver.pojo.IMessageType;
import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.udptransfer.TransferPacket;

public abstract class Message {

    protected static final int MESSAGE_TOKEN_LEN = 16;
    private static final int HEAD_LEN = Integer.BYTES;
    private final Request request;
    private UserSession userSession;

    public Message(Request request) throws MessageResolutionException {
        this.request = request;
        decode(request);
    }

    public String getToken() {
        return null;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public static int getHeadLength() {
        return HEAD_LEN + Request.getHeadLength();
    }

    public abstract MessageType getType();
    protected abstract void decode(Request request) throws MessageResolutionException;

    public static Message decodeFromRequest(Request request) {
        TransferPacket.BodyBuffer buffer = request.getBodyBuffer();
        buffer.position(Request.getHeadLength());
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
