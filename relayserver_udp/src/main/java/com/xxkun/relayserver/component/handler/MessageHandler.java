package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.pojo.IMessageType;
import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.relayserver.pojo.request.Message;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageHandler {

    private static Map<IMessageType, MessageHandler> map = new HashMap<>();

    public MessageHandler() {
        map.put(getMessageType(), this);
    }

    public static MessageHandler getInstance(IMessageType type) {
        return map.get(type);
    }

    public abstract IMessageType getMessageType();

    public abstract void consume(Message message);
}
