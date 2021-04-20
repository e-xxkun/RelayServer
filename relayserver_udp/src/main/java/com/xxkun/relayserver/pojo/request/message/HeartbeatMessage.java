package com.xxkun.relayserver.pojo.request.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.pojo.IMessageType;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.udptransfer.TransferPacket;

import java.nio.BufferUnderflowException;

public class HeartbeatMessage extends Message {

    public HeartbeatMessage(Request request) throws MessageResolutionException {
        super(request);
    }

    @Override
    public IMessageType getType() {
        return MessageType.GET.HEARTBEAT;
    }

    @Override
    protected void decode(Request udpField) {
    }
}
