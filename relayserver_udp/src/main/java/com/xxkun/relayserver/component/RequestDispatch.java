package com.xxkun.relayserver.component;

import com.xxkun.relayserver.component.handler.ACKHandler;
import com.xxkun.relayserver.component.handler.RequestHandler;
import com.xxkun.relayserver.dao.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
public class RequestDispatch implements RequestListener.OnRequest {
    @Autowired
    private ACKHandler ackMsgHandler;
    @Autowired
    private RequestHandler requestHandler;

    @Override
    public void onRequest(SocketAddress from, Request request) {
        switch (request.getType()) {
            case ACK:
                ackMsgHandler.consume(request);
                break;
            case PUT:
            case GET:
                requestHandler.consume(request);
                break;
        }
    }
}
