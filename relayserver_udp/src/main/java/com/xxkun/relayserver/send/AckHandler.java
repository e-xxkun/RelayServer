package com.xxkun.relayserver.send;

import com.xxkun.relayserver.dao.response.AckResponse;
import com.xxkun.relayserver.dao.response.Response;
import com.xxkun.relayserver.dao.request.Request;
import com.xxkun.relayserver.dto.AckResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AckHandler {
    @Autowired
    private ResponsePool responsePool;
    @Autowired
    private ResponseSender responseSender;

    public void consume(Request request) {
        Response response = responsePool.createResponse(request.getSocketAddress(), request.getSequence());
        responsePool.ack(response);
    }

    public void replySuccess(Request request) {
        AckResponse response = responsePool.createAckResponse(request.getSocketAddress(), request.getSequence());
        response.setType(AckResponseType.SUCCESS);
        responseSender.send(response, false);
    }

    public void replyUnknown(Request request) {
        AckResponse response = responsePool.createAckResponse(request.getSocketAddress(), request.getSequence());
        response.setType(AckResponseType.UNKNOWN);
        responseSender.send(response, false);
    }

    public void replyLoginExpire(Request request) {
        AckResponse response = responsePool.createAckResponse(request.getSocketAddress(), request.getSequence());
        response.setType(AckResponseType.LOGIN_EXPIRE);
        responseSender.send(response, false);
    }
}