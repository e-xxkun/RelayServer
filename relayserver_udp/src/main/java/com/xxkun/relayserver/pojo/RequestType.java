package com.xxkun.relayserver.pojo;

public enum RequestType {
    GET(0) {
        @Override
        public boolean isGET() {
            return true;
        }
    },
    PUT(1) {
        @Override
        public boolean isPUT() {
            return true;
        }
    };

    final int code;

    RequestType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static RequestType fromCode(int code) {
        if (code == 1) {
            return PUT;
        }
        return GET;
    }

    public boolean isGET() {
        return false;
    }

    public boolean isPUT() {
        return false;
    }
}
