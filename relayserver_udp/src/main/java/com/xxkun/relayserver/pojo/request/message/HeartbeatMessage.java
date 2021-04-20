package com.xxkun.relayserver.pojo.request.message;

import com.xxkun.relayserver.component.exception.MessageResolutionException;
import com.xxkun.relayserver.pojo.IInnerMessageType;
import com.xxkun.relayserver.pojo.MessageFactory;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.Request;

public class HeartbeatMessage extends Message {

    IInnerMessageType type = MessageFactory.GET.HEARTBEAT;

    public HeartbeatMessage(Request request) throws MessageResolutionException {
        super(request);
    }

    public void setType(IInnerMessageType type) {
        this.type = type;
    }

    @Override
    public IInnerMessageType getType() {
        return type;
    }
}
