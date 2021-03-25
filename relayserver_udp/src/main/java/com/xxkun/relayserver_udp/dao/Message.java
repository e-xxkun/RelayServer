package com.xxkun.relayserver_udp.dao;

import reactor.util.annotation.NonNull;

import java.net.InetSocketAddress;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Message {

    private final UDPField udpField;

    public Message(@NonNull UDPField udpField) {
        this.udpField = udpField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return udpField.equals(message.udpField);
    }

    @Override
    public int hashCode() {
        return udpField.hashCode();
    }

    public abstract UDPField convertToUDPField();

    public abstract InetSocketAddress getSocketAddress();
}
