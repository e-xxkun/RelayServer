package com.xxkun.relayserver.dto;

public enum ReplyResponseType {
    UNKNOWN(0),
    SUCCESS(1),
    LOGIN_EXPIRE(2),

    USER_NOT_EXIST(3),
    USER_OFFLINE(4);

    final int code;

    ReplyResponseType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public int length() {
        return Integer.BYTES;
    }
}
