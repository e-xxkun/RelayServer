package com.xxkun.relayserver_udp.dao;

import com.xxkun.relayserver_udp.dto.MessageType;

import java.net.InetSocketAddress;

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
