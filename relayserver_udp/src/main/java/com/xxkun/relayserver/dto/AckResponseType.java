package com.xxkun.relayserver.dto;

public enum  AckResponseType {
    SUCCESS(0),
    LOGIN_EXPIRE(1),
    UNKNOWN(2);

    final int code;

    AckResponseType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public int length() {
        return Integer.BYTES;
    }
}
