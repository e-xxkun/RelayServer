package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.component.ResponsePool;
import com.xxkun.relayserver.dao.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ACKHandler implements IRequestHandler {
    @Autowired
    private ResponsePool responsePool;

    @Override
    public void consume(Request request) {
//        TODO
//        responsePool.ack(request);
    }
}
