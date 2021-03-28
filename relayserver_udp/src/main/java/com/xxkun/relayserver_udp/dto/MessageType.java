package com.xxkun.relayserver_udp.dto;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dao.message.HeartbeatMessage;

public enum  MessageType {

    LOGIN(0L, " "),
    LOGOUT(1, " "),
    LIST(2, ""),
    PUNCH(3, ""),
    HEARTBEAT(4, " "){
        @Override
        public Message createMessage(UDPField udpField) {
            return new HeartbeatMessage(udpField);
        }
    },
    REPLY(5, ""),
    TEXT(6, " "),
    UNKNOWN(7, " ");

    private final long code;
    private final String info;

    MessageType(long code, String info) {
        this.code = code;
        this.info = info;
    }

    public static MessageType fromTypeValue(int type) {
        if (type < 0 || type > MessageType.values().length) {
            return UNKNOWN;
        }
        return MessageType.values()[type];
    }

    public String getInfo() {
        return info;
    }

    public long getCode() {
        return code;
    }

    public Message createMessage(UDPField udpField) {
        return null;
    }
}
