package com.xxkun.relayserver_udp.dao;

import com.xxkun.relayserver_udp.dto.IMessageType;
import com.xxkun.relayserver_udp.dto.MessageType;
import reactor.util.annotation.NonNull;

import java.net.InetSocketAddress;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Message {

    public static final int UDP_MSG_IN_BUFF_LEN = 512;
    
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

    public InetSocketAddress getSocketAddress(){
        return udpField.getSocketAddress();
    }

    public boolean isACK() {
        return udpField.isACK();
    }

    public abstract String getToken();

    public abstract MessageType getType();
}
