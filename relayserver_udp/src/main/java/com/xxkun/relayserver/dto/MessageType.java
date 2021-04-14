package com.xxkun.relayserver.dto;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.component.handler.HeartbeatMessageHandler;
import com.xxkun.relayserver.component.handler.IMessageHandler;
import com.xxkun.relayserver.component.handler.PunchMessageHandler;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.Request;
import com.xxkun.relayserver.dao.request.message.HeartbeatMessage;
import com.xxkun.relayserver.dao.request.message.PunchMessage;
import org.springframework.beans.factory.annotation.Autowired;

public enum  MessageType implements IMessageType {

    PUNCH(0, "") {
        @Autowired
        private PunchMessageHandler punchMsgHandler;
        @Override
        public Message createMessage(Request request) throws MessageResolutionException {
            return new PunchMessage(request);
        }
        @Override
        public IMessageHandler getMessageHandler() {
            return punchMsgHandler;
        }
    },
    HEARTBEAT(1, ""){
        @Autowired
        private HeartbeatMessageHandler heartbeatMsgHandler;
        @Override
        public Message createMessage(Request request) throws MessageResolutionException {
            return new HeartbeatMessage(request);
        }
        @Override
        public IMessageHandler getMessageHandler() {

            return heartbeatMsgHandler;
        }
    },
    REPLY(2, ""),
    UNKNOWN(3, "");

    private final int code;
    private final String info;

    MessageType(int code, String info) {
        this.code = code;
        this.info = info;
    }

    static {
        for (MessageType type : MessageType.values()) {
            typeMap.put(type.code, type);
        }
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Message createMessage(Request request) throws MessageResolutionException {
        return null;
    }

    @Autowired
    public IMessageHandler getMessageHandler() {
        return null;
    }
}
