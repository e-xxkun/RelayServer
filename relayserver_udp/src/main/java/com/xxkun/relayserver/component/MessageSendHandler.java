package com.xxkun.relayserver.component;

import com.xxkun.relayserver.component.handler.IMessageHandler;
import com.xxkun.relayserver.component.queue.IMessageQueue;
import com.xxkun.relayserver.dao.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageSendHandler implements IMessageQueue.OnMessage{

    @Override
    public void onMessage(Message msg) {
        IMessageHandler messageHandler = msg.getType().getMessageHandler();
        if (messageHandler != null) {
            messageHandler.consume(msg);
        }
    }
}
