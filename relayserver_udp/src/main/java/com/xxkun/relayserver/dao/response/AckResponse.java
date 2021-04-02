package com.xxkun.relayserver.dao.response;

import java.net.InetSocketAddress;

public class AckResponse extends Response{
    public AckResponse(InetSocketAddress socketAddress) {
        super(socketAddress);
    }

    @Override
    public int getBodyLength() {
        return 0;
    }

    @Override
    public ResponseType getType() {
        return ResponseType.ACK;
    }
}
