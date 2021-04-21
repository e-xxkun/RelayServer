package com.xxkun.relayserver.pojo.request;

import com.xxkun.relayserver.component.exception.RequestResolutionException;
import com.xxkun.relayserver.pojo.RequestType;
import com.xxkun.udptransfer.TransferPacket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Date;

public final class Request {

    private static final int TOKEN_LEN = 16;
    private final static int HEAD_LEN = 2 * Integer.BYTES + TOKEN_LEN * Character.BYTES;
    private final InetSocketAddress socketAddress;
    private final int clientVersion;
    private final RequestType type;
    private final TransferPacket.BodyBuffer bodyBuffer;
    private final String token;

    private Request(TransferPacket.BodyBuffer buffer, int clientVersion, RequestType type, String token, InetSocketAddress socketAddress) {
        this.bodyBuffer = buffer;
        this.type = type;
        this.socketAddress = socketAddress;
        this.clientVersion = clientVersion;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public int getBodyLength() {
        return bodyBuffer.getBodyLength() - HEAD_LEN;
    }

    public RequestType getType() {
        return type;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public TransferPacket.BodyBuffer getBodyBuffer() {
        bodyBuffer.position(HEAD_LEN);
        return bodyBuffer;
    }

    public static int getHeadLength() {
        return HEAD_LEN;
    }

    public static Request decodeFromByteArray(TransferPacket.BodyBuffer buffer, InetSocketAddress socketAddress) throws RequestResolutionException {
        if (buffer.getBodyLength() < HEAD_LEN) {
            throw new RequestResolutionException();
        }
        // client_version|type|token  ->  int|int|char[16]
        int clientVersion;
        int code;
        String token;
        try {
            clientVersion = buffer.getInt();
            token = buffer.getString(TOKEN_LEN);
            code = buffer.getInt();
        } catch (BufferUnderflowException e) {
            throw new RequestResolutionException();
        }
        if (clientVersion < 0 || code < 0 || code >= RequestType.values().length) {
            throw new RequestResolutionException();
        }

        return new Request(buffer, clientVersion, RequestType.fromCode(code), token, socketAddress);
    }
}
