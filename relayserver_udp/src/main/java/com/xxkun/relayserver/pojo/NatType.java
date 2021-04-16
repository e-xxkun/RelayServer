package com.xxkun.relayserver.pojo;

import com.xxkun.relayserver.pojo.user.UserStatus;

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

    public static NatType fromCode(int code) {
        if (code < 0 || code >= UserStatus.values().length) {
            return NatType.values()[code];
        }
        return SYMMETRIC;
    }
}
