package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.component.handler.ACKHandler;
import com.xxkun.relayserver_udp.component.handler.RequestHandler;
import com.xxkun.relayserver_udp.dao.UDPField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
public class MessageReceiveHandler implements MessageListener.OnMessage {
    @Autowired
    private ACKHandler ackMsgHandler;
    @Autowired
    private RequestHandler requestHandler;

    @Override
    public void onMessage(SocketAddress from, UDPField udpField) {
        switch (udpField.getType()) {
            case ACK:
                ackMsgHandler.consume(udpField);
                break;
            case PUT:
            case GET:
                requestHandler.consume(udpField);
                break;
        }
    }
}
