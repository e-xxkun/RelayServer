package com.xxkun.relayserver.pojo;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.component.handler.MessageHandler;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;

import java.util.HashMap;

public interface IMessageType {

    HashMap<Integer, IMessageType> typeMap = new HashMap<>();

    static IMessageType fromTypeCode(int type) {
        return typeMap.get(type);
    }

    String getInfo();

    int getCode();

    Message createMessage(Request request) throws MessageResolutionException;

    MessageHandler getMessageHandler();
}
