package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.relayserver.pojo.response.HeartbeatResponse;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.send.ResponsePool;
import com.xxkun.relayserver.send.ResponseSender;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatMessageHandler extends MessageHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Autowired
    private ResponsePool responsePool;
    @Autowired
    private ResponseSender responseSender;

    @Override
    public MessageType getMessageType() {
        return MessageType.HEARTBEAT;
    }

    @Override
    public void consume(Message message) {
        System.out.println("HEARTBEAT: " + message);
        if (!userInfoManageService.isUserSessionExpire(message.getUserSession())) {
            return;
        }
        UserInfo userInfo = userInfoManageService.refreshUserSession(message.getUserSession());
        HeartbeatResponse response = responsePool.createHeartbeatResponse(message.getRequest().getSocketAddress());
        response.setUserInfo(userInfo);
        responseSender.send(response);
    }
}
