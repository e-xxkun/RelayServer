package com.xxkun.relayserver.pojo;

public enum ResponseType {
    UNKNOWN(0),
    SUCCESS(1),
    LOGIN_EXPIRE(2),

    USER_EXCEPTION(8),

    UPDATE_TOKEN(5),

    PUNCH(6);

    final int code;

    ResponseType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public enum UserExceptionType {
        USER_NOT_EXIST(3),
        USER_OFFLINE(4);

        final int code;
        UserExceptionType(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }
}
