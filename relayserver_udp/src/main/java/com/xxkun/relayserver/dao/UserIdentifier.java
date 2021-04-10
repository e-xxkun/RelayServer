package com.xxkun.relayserver.dao;

import java.util.UUID;

public class UserIdentifier {

    public static final int IDENTIFIER_LEN = 8;

    private String identifier;

    public UserIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean check(String identifier) {
        return this.identifier.regionMatches(0, identifier, 0, IDENTIFIER_LEN)
                || this.identifier.regionMatches(IDENTIFIER_LEN, identifier, 0, IDENTIFIER_LEN);
    }

    public String update() {
        String token = UUID.randomUUID().toString();
        identifier = token.substring(0, IDENTIFIER_LEN) + identifier.substring(0, IDENTIFIER_LEN);
        return identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
