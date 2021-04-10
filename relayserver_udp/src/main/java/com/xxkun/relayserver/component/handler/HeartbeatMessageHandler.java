package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.message.HeartbeatMessage;
import com.xxkun.relayserver.dao.response.HeartbeatResponse;
import com.xxkun.relayserver.send.ResponsePool;
import com.xxkun.relayserver.send.ResponseSender;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatMessageHandler implements IMessageHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Autowired
    private ResponsePool responsePool;
    @Autowired
    private ResponseSender responseSender;
    @Override
    public void consume(Message message) {
//        TODO
        if (!userInfoManageService.isUserSessionExpire(message.getUserSession())) {
            return;
        }
        UserInfo userInfo = userInfoManageService.refreshUserSession(message.getUserSession());
        HeartbeatResponse response = responsePool.createHeartbeatResponse(message.getRequest().getSocketAddress());
        response.setUserInfo(userInfo);
        responseSender.send(response);
    }
}
