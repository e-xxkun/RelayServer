package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.component.MessageCache;
import com.xxkun.relayserver_udp.dao.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ACKMsgHandler implements IHandler{
    @Autowired
    private MessageCache messageCache;
    @Override
    public void consume(Message msg) {
        messageCache.ack(msg.convertToUDPField());
    }
}
