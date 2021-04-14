package com.xxkun.relayserver.common;

import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class TokenProducer {
    @Value("common.token.length")
    private static int TOKEN_LENGTH;

    public static String get(long userId) {
        String token = UUID.randomUUID().toString();
        token = token.substring(0, TOKEN_LENGTH - Long.BYTES / Character.BYTES) + Utils.longToString(userId);
        return token;
    }
}
