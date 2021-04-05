package com.xxkun.relayserver.dao.response;

import com.xxkun.relayserver.dto.ReplyResponseType;

import java.net.InetSocketAddress;

public class AckResponse extends Response{

    private ReplyResponseType type = ReplyResponseType.SUCCESS;

    public AckResponse(InetSocketAddress socketAddress, long sequence) {
        super(socketAddress);
        setSequence(sequence);
    }

    public void setType(ReplyResponseType type) {
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
