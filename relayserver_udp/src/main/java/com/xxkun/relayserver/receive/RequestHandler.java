package com.xxkun.relayserver.receive;

import com.xxkun.relayserver.send.ReplyHandler;
import com.xxkun.relayserver.component.queue.IMessageQueue;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.Request;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
public class RequestHandler implements RequestListener.OnRequest {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Qualifier("putMessageQueue")
    @Autowired
    private IMessageQueue putMsgQueueSender;
    @Qualifier("getMessageQueue")
    @Autowired
    private IMessageQueue getMsgQueueSender;
    @Autowired
    private ReplyHandler replyHandler;

    @Override
    public void onRequest(SocketAddress from, Request request) {
        if (request.getType().isACK()) {
            replyHandler.consume(request);
            return;
        }

        Message message = Message.decodeFromRequest(request);
        if (message == null) {
            replyHandler.replyUnknown(request);
            return;
        }
        UserSession userSession = userInfoManageService.getUserSessionFromToken(message.getToken());
        if (userSession == null) {
            replyHandler.replyLoginExpire(request);
            return;
        }
        message.setUserSession(userSession);
        if (request.getType().isGET()) {
            getMsgQueueSender.sendMessage(message);
        } else if (request.getType().isPUT()){
            putMsgQueueSender.sendMessage(message);
        }
        replyHandler.replySuccess(request);
    }
}
