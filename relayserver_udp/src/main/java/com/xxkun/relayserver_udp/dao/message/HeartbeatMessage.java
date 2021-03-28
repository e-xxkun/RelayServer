package com.xxkun.relayserver_udp.dao.message;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dto.MessageType;

public class HeartbeatMessage extends Message {

    public HeartbeatMessage(UDPField udpField) {
        super(udpField);
    }

    @Override
    public UDPField convertToUDPField() {
        return null;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public MessageType getType() {
        return null;
    }
}
