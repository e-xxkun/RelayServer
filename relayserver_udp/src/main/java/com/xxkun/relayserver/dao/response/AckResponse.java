package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.dto.AckResponseType;

import java.net.InetSocketAddress;

public class AckResponse extends Response{

    private AckResponseType type = AckResponseType.UNKNOWN;

    public AckResponse(InetSocketAddress socketAddress, long sequence) {
        super(socketAddress);
        setSequence(sequence);
    }

    public void setType(AckResponseType type) {
        this.type = type;
    }

    @Override
    public int getBodyLength() {
        return type.length();
    }

    @Override
    public ResponseType getType() {
        return ResponseType.ACK;
    }

    @Override
    protected void overwriteToByteArray(BodyBuffer bodyBuffer) {
        bodyBuffer.position(0);
        bodyBuffer.writeInt(type.getCode());
    }
}
