package com.xxkun.relayserver_udp.dao;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UClient {
    private Map<UDPField, Object> msgSet;

    private InetSocketAddress socketAddress;

    private AtomicLong curSeq;

    public UClient(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        this.curSeq = new AtomicLong(new Random().nextLong() % 256);
        msgSet = new ConcurrentHashMap<>();
    }

    public void addUDPMsg(UDPField udpMsg) {
        msgSet.put(udpMsg, null);
    }

    public boolean removeMessage(UDPField udpMsg) {
        return true;
    }

    public boolean containsMessage(UDPField udpMsg) {
        return msgSet.containsKey(udpMsg);
    }

    public long getCurSeq() {
        return curSeq.getAndIncrement();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UClient client = (UClient) o;
        return socketAddress.equals(client.socketAddress);
    }

    @Override
    public int hashCode() {
        return socketAddress.hashCode();
    }

    public int msgSize() {
        return msgSet.size();
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

}
