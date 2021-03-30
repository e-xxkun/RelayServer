package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.component.handler.HeartbeatMsgHandler;
import com.xxkun.relayserver_udp.component.queue.IMsgQueue;
import com.xxkun.relayserver_udp.dao.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSendHandler implements IMsgQueue.OnMessage{

    @Autowired
    private HeartbeatMsgHandler heartbeatMsgHandler;

    @Override
    public void onMessage(Message msg) {
        switch (msg.getType()) {
            case PUNCH:
                break;
            case HEARTBEAT:
                heartbeatMsgHandler.consume(msg);
                break;
            case REPLY:
                break;
            case UNKNOWN:
                break;
        }

    }
}
