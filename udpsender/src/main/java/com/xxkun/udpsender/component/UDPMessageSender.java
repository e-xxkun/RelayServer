package com.xxkun.udpsender.component;

import com.xxkun.udpsender.dao.UDPField;

import java.net.DatagramSocket;

public class UDPMessageSender {

    private final DatagramSocket socket;

    public UDPMessageSender(DatagramSocket socket) {
        this.socket = socket;
    }

    public void send(UDPField udpField) {

    }
}
