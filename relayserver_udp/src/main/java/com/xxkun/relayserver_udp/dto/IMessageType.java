package com.xxkun.relayserver_udp.dto;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;

import java.util.HashMap;

public interface IMessageType {

    HashMap<Long, IMessageType> typeMap = new HashMap<>();

    static IMessageType fromTypeCode(long type) {
        return typeMap.get(type);
    }

    String getInfo();

    long getCode();

    Message createMessage(UDPField udpField);
}
