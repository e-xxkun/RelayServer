package com.xxkun.relayserver.pojo;

public enum InnerMessageType implements IInnerMessageType {
    REFRESH_SESSION {
        @Override
        public boolean isPUT() {
            return true;
        }
    },
    ;
}
