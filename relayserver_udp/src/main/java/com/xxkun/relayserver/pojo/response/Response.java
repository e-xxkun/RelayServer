package com.xxkun.relayserver.pojo.response;

import com.xxkun.relayserver.pojo.ResponseType;
import com.xxkun.udptransfer.TransferPacket;

import java.net.InetSocketAddress;

public abstract class Response{

//    type  ->  int
    private static final int HEAD_LEN = Integer.BYTES;

    private InetSocketAddress socketAddress;

    private final TransferPacket.BodyBuffer bodyBuffer;

    public Response(InetSocketAddress socketAddress) {
        setSocketAddress(socketAddress);
        bodyBuffer = new TransferPacket.BodyBuffer(length());
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    private int length() {
        return HEAD_LEN + getBodyLength();
    }

    public boolean hashNext() {
        return false;
    }

    public Response next() {
        return null;
    }

    public abstract ResponseType getType();

    protected abstract int getBodyLength();

    protected abstract void overwrite(TransferPacket.BodyBuffer bodyBuffer);

    public TransferPacket.BodyBuffer getBodyBuffer() {
        bodyBuffer.position(Integer.BYTES);
        overwrite(bodyBuffer);
        bodyBuffer.position(0);
        bodyBuffer.putInt(getType().getCode());
        return bodyBuffer;
    }
}
