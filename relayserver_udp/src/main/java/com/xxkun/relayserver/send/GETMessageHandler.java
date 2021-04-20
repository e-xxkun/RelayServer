package com.xxkun.relayserver.send;

import com.xxkun.relayserver.component.handler.MessageHandler;
import com.xxkun.relayserver.component.queue.IMessageQueue;
import com.xxkun.relayserver.pojo.request.Message;
import org.springframework.stereotype.Component;

@Component
public class GETMessageHandler implements IMessageQueue.OnMessage{

    @Override
    public void onMessage(Message msg) {
        MessageHandler messageHandler = MessageHandler.getGETMessageHandler(msg.getType());
        if (messageHandler != null) {
            messageHandler.consume(msg);
        }
    }
}
