package com.xxkun.relayserver_udp.dto;

import com.xxkun.relayserver_udp.component.exception.MessageResolutionException;
import com.xxkun.relayserver_udp.component.handler.HeartbeatMsgHandler;
import com.xxkun.relayserver_udp.component.handler.IMessageHandler;
import com.xxkun.relayserver_udp.component.handler.PunchMsgHandler;
import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dao.message.HeartbeatMessage;
import com.xxkun.relayserver_udp.dao.message.PunchMessage;
import org.springframework.beans.factory.annotation.Autowired;

public enum  MessageType implements IMessageType {

    PUNCH(3, "") {
        @Override
        public Message createMessage(UDPField udpField) throws MessageResolutionException {
            return new PunchMessage(udpField);
        }
        @Override
        public IMessageHandler getMessageHandler() {
            return PUNCH.punchMsgHandler;
        }
    },
    HEARTBEAT(4, ""){
        @Override
        public Message createMessage(UDPField udpField) throws MessageResolutionException {
            return new HeartbeatMessage(udpField);
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
    private HeartbeatMsgHandler heartbeatMsgHandler;
    @Autowired
    private PunchMsgHandler punchMsgHandler;

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
    public Message createMessage(UDPField udpField) throws MessageResolutionException {
        return null;
    }

    @Autowired
    public IMessageHandler getMessageHandler() {
        return null;
    }
}
