package com.xxkun.relayserver.pojo;

import com.xxkun.relayserver.pojo.response.Response;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserClient {
    private final Set<Response> responseSet;

    private final InetSocketAddress socketAddress;

    private final AtomicLong curSequence;

    public UserClient(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        this.curSequence = new AtomicLong(new Random().nextLong() % 256);
        responseSet = ConcurrentHashMap.newKeySet();
    }

    public void addResponse(Response response) {
        responseSet.add(response);
    }

    public boolean removeResponse(Response response) {
        return responseSet.remove(response);
    }

    public boolean containsResponse(Response response) {
        return responseSet.contains(response);
    }

    public long getCurSequence() {
        return curSequence.getAndIncrement();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClient client = (UserClient) o;
        return socketAddress.equals(client.socketAddress);
    }

    @Override
    public int hashCode() {
        return socketAddress.hashCode();
    }

    public int responseCount() {
        return responseSet.size();
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

}
