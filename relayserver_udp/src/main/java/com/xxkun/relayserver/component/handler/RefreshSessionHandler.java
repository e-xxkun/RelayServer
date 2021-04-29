package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.component.ResponsePool;
import com.xxkun.relayserver.pojo.IInnerMessageType;
import com.xxkun.relayserver.pojo.IMessageType;
import com.xxkun.relayserver.pojo.InnerMessageType;
import com.xxkun.relayserver.pojo.MessageFactory;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.response.UpdateTokenResponse;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.send.ResponseSender;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshSessionHandler extends MessageHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Autowired
    private ResponseSender responseSender;
    @Override
    public IInnerMessageType getInnerMessageType() {
        return InnerMessageType.REFRESH_SESSION;
    }

    @Override
    public void consume(Message message) {
        UserInfo userInfo = userInfoManageService.refreshUserSession(message.getUserSession());
        UpdateTokenResponse response = ResponsePool.createHeartbeatResponse(message.getRequest().getSocketAddress());
        response.setUserInfo(userInfo);
        responseSender.send(response);
    }
}
