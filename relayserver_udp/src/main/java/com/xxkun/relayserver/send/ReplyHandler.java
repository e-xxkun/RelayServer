package com.xxkun.relayserver.send;

import com.xxkun.relayserver.dao.response.AckResponse;
import com.xxkun.relayserver.dao.response.Response;
import com.xxkun.relayserver.dao.request.Request;
import com.xxkun.relayserver.dto.ReplyResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplyHandler {
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
        response.setType(ReplyResponseType.SUCCESS);
        responseSender.send(response, false);
    }

    public void replyUnknown(Request request) {
        AckResponse response = responsePool.createAckResponse(request.getSocketAddress(), request.getSequence());
        response.setType(ReplyResponseType.UNKNOWN);
        responseSender.send(response, false);
    }

    public void replyLoginExpire(Request request) {
        AckResponse response = responsePool.createAckResponse(request.getSocketAddress(), request.getSequence());
        response.setType(ReplyResponseType.LOGIN_EXPIRE);
        responseSender.send(response, false);
    }
}