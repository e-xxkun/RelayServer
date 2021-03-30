package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.dao.Message;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatMsgHandler implements IMessageHandler {
    @Override
    public void consume(Message msg) {

    }
}
