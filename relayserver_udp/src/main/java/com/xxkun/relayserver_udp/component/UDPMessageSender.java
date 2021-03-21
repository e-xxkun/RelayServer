package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UDPField;

import java.net.DatagramSocket;

public class UDPMessageSender {

    private final DatagramSocket socket;

    public UDPMessageSender(DatagramSocket socket) {
        this.socket = socket;
    }

    public void send(UDPField udpField) {

    }
}
