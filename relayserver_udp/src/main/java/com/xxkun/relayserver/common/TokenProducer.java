package com.xxkun.relayserver.common;

import java.util.UUID;

public class TokenProducer {

    private final static Integer TOKEN_LENGTH = 16;

    public static String get(long userId) {
        String token = UUID.randomUUID().toString();
//        token = token.substring(0, TOKEN_LENGTH - Long.BYTES / Character.BYTES) + Utils.longToString(userId);
        token = token.substring(0, TOKEN_LENGTH);
        return token;
    }
}
