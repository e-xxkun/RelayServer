package com.xxkun.relayserver.send;

import com.xxkun.relayserver.component.handler.IMessageHandler;
import com.xxkun.relayserver.component.queue.IMessageQueue;
import com.xxkun.relayserver.dao.request.Message;
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
