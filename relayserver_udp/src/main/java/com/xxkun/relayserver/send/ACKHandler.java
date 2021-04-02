package com.xxkun.relayserver.send;

import com.xxkun.relayserver.send.ResponsePool;
import com.xxkun.relayserver.dao.request.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ACKHandler {
    @Autowired
    private ResponsePool responsePool;

    public void consume(Request request) {
//        TODO
//        responsePool.ack(request);
    }

    public void reply(Request request) {

    }
}