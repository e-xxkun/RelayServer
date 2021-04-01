package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.component.queue.IMessageQueue;
import com.xxkun.relayserver.dao.Message;
import com.xxkun.relayserver.dao.Request;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RequestHandler implements IRequestHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Qualifier("putMessageQueue")
    @Autowired
    private IMessageQueue putMsgQueueSender;
    @Qualifier("getMessageQueue")
    @Autowired
    private IMessageQueue getMsgQueueSender;
    
    @Override
    public void consume(Request request) {
        Message message = Message.decodeFromRequest(request);
        if (message == null) {
            return;
        }
        UserSession userSession = userInfoManageService.getUserSessionFromToken(message.getToken());
        if (userSession == null) {
            return;
        }
        if (request.getType().isGET()) {
            getMsgQueueSender.sendMessage(message);
        } else if (request.getType().isPUT()){
            putMsgQueueSender.sendMessage(message);
        }
    }
}
