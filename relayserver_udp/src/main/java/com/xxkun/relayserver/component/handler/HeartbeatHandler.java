package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.component.queue.IMessageQueue;
import com.xxkun.relayserver.pojo.IInnerMessageType;
import com.xxkun.relayserver.pojo.InnerMessageType;
import com.xxkun.relayserver.pojo.MessageFactory;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.message.HeartbeatMessage;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatHandler extends MessageHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Qualifier("putMessageQueue")
    @Autowired
    private IMessageQueue putMsgQueueSender;

    @Override
    public IInnerMessageType getInnerMessageType() {
        return MessageFactory.GET.HEARTBEAT;
    }

    @Override
    public void consume(Message message) {
        System.out.println("HEARTBEAT: " + message);
        if (!userInfoManageService.isUserSessionExpire(message.getUserSession())) {
            return;
        }
        ((HeartbeatMessage) message).setType(InnerMessageType.REFRESH_SESSION);
        putMsgQueueSender.sendMessage(message);
    }
}
