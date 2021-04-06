package com.xxkun.relayserver.dto;

public enum NatType {
    SYMMETRIC(0),
    FULL_CONE(1),
    RESTRICTED_CONE(2),
    PORT_RESTRICTED_CONE(3);

    int code;

    NatType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
