package com.xxkun.relayserver_udp.dao;

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
    public InetSocketAddress getSocketAddress() {
        return null;
    }
}
