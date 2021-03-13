package com.xxkun.udpsender.dao;

import reactor.util.annotation.NonNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Message implements Delayed {

    private final UDPField udpField;

    public Message(@NonNull UDPField udpField) {
        this.udpField = udpField;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return udpField.getSendDate();
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
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
}
