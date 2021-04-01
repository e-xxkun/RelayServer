package com.xxkun.relayserver.dto;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.component.handler.IMessageHandler;
import com.xxkun.relayserver.dao.Message;
import com.xxkun.relayserver.dao.Request;

import java.util.HashMap;

public interface IMessageType {

    HashMap<Long, IMessageType> typeMap = new HashMap<>();

    static IMessageType fromTypeCode(long type) {
        return typeMap.get(type);
    }

    String getInfo();

    long getCode();

    Message createMessage(Request request) throws MessageResolutionException;

    IMessageHandler getMessageHandler();
}
