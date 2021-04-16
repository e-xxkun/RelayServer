package com.xxkun.relayserver.pojo.user;

public enum UserStatus {
    ONLINE(0) {
        @Override
        public boolean isOnline() {
            return true;
        }
    },
    OFFLINE(1) {
        @Override
        public boolean isOffline() {
            return true;
        }
    },
    INVISIBLE(2) {
        @Override
        public boolean isInvisible() {
            return true;
        }
    };

    int code;
    UserStatus(int code) {
        this.code = code;
    }

    public boolean isOnline() {
        return false;
    }

    public boolean isOffline() {
        return false;
    }
    public boolean isInvisible() {
        return false;
    }

    public static UserStatus fromCode(int code) {
        if (code < 0 || code >= UserStatus.values().length) {
            return UserStatus.values()[code];
        }
        return OFFLINE;
    }
}
