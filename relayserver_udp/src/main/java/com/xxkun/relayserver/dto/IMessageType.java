package com.xxkun.relayserver.dto;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.component.handler.IMessageHandler;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.Request;

import java.util.HashMap;

public interface IMessageType {

    HashMap<Integer, IMessageType> typeMap = new HashMap<>();

    static IMessageType fromTypeCode(int type) {
        return typeMap.get(type);
    }

    String getInfo();

    int getCode();

    Message createMessage(Request request) throws MessageResolutionException;

    IMessageHandler getMessageHandler();
}
