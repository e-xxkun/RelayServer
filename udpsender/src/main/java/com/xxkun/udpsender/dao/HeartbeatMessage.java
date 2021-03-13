package com.xxkun.udpsender.dao;

public class HeartbeatMessage extends Message {

    public HeartbeatMessage(UDPField udpField) {
        super(udpField);
    }

    @Override
    public UDPField convertToUDPField() {
        return null;
    }
}
