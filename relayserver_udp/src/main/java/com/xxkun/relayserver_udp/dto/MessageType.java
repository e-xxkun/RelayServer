package com.xxkun.relayserver_udp.dto;

public enum  MessageType implements IMessageType{

    LOGIN(0L, " "),
    LOGOUT(1, " "),
    LIST(2, ""),
    PUNCH(3, ""),
    HEARTBEAT(4, " "),
    REPLY(5, ""),
    TEXT(6, " "),
    UNKNOWN(7, " ");

    private final long code;
    private final String message;

    MessageType(long code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public long getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
