package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.relayserver.pojo.request.Message;

import java.util.EnumMap;

public abstract class MessageHandler {

    private static EnumMap<MessageType, MessageHandler> map = new EnumMap<>(MessageType.class);

    public MessageHandler() {
        map.put(getMessageType(), this);
    }

    public static MessageHandler getInstance(MessageType type) {
        return map.get(type);
    }

    public abstract MessageType getMessageType();

    public abstract void consume(Message message);
}
