package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.pojo.IInnerMessageType;
import com.xxkun.relayserver.pojo.request.Message;

import java.util.HashMap;
import java.util.Map;

public abstract class MessageHandler {

    private static Map<IInnerMessageType, MessageHandler> putMap = new HashMap<>();
    private static Map<IInnerMessageType, MessageHandler> getMap = new HashMap<>();

    public MessageHandler() {
        IInnerMessageType type = getInnerMessageType();
        if (type.isPUT()) {
            putMap.put(type, this);
        } else {
            getMap.put(type, this);
        }
    }

    public static MessageHandler getPUTMessageHandler(IInnerMessageType type) {
        return putMap.get(type);
    }

    public static MessageHandler getGETMessageHandler(IInnerMessageType type) {
        return getMap.get(type);
    }

    public static MessageHandler getInstanceFromMessageType(IInnerMessageType type) {
        return type.isPUT() ? putMap.get(type) : getMap.get(type);
    }

    public abstract IInnerMessageType getInnerMessageType();

    public abstract void consume(Message message);
}
