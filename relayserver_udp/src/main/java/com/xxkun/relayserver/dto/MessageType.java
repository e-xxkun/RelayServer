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

    PUNCH(3, "") {
        @Override
        public Message createMessage(Request request) throws MessageResolutionException {
            return new PunchMessage(request);
        }
        @Override
        public IMessageHandler getMessageHandler() {
            return PUNCH.punchMsgHandler;
        }
    },
    HEARTBEAT(4, ""){
        @Override
        public Message createMessage(Request request) throws MessageResolutionException {
            return new HeartbeatMessage(request);
        }
        @Override
        public IMessageHandler getMessageHandler() {
            return HEARTBEAT.heartbeatMsgHandler;
        }
    },
    REPLY(5, ""),
    UNKNOWN(7, "");

    private final long code;
    private final String info;

    @Autowired
    private HeartbeatMessageHandler heartbeatMsgHandler;
    @Autowired
    private PunchMessageHandler punchMsgHandler;

    MessageType(long code, String info) {
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
    public long getCode() {
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
