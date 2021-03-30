package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.component.MessageCache;
import com.xxkun.relayserver_udp.dao.UDPField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ACKHandler implements IUDPFieldHandler {
    @Autowired
    private MessageCache messageCache;

    @Override
    public void consume(UDPField udpField) {
        messageCache.ack(udpField);
    }
}
