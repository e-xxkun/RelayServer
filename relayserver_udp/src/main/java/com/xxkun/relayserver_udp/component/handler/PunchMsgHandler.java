package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dto.MessageType;
import org.springframework.stereotype.Component;

@Component
public class PunchMsgHandler implements IMessageHandler{
    @Override
    public void consume(Message msg) {

    }
}
