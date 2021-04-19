package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.pojo.MessageType;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.request.Message;
import com.xxkun.relayserver.pojo.request.message.PunchMessage;
import com.xxkun.relayserver.pojo.response.PunchResponse;
import com.xxkun.relayserver.pojo.response.UserExceptionResponse;
import com.xxkun.relayserver.pojo.ResponseType;
import com.xxkun.relayserver.send.ResponsePool;
import com.xxkun.relayserver.send.ResponseSender;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PunchMessageHandler extends MessageHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Autowired
    private ResponsePool responsePool;
    @Autowired
    private ResponseSender responseSender;

    @Override
    public MessageType getMessageType() {
        return MessageType.PUNCH;
    }

    @Override
    public void consume(Message message) {
        PunchMessage punchMessage = (PunchMessage) message;

//        TODO Judge user status
        UserInfo user = userInfoManageService.getUserInfoFromUserSession(message.getUserSession());

        UserInfo[] userInfos = punchMessage.getUserInfos();
        List<UserInfo> notExistUsers = new ArrayList<>();
        List<UserInfo> offloadUsers = new ArrayList<>();
        List<UserInfo> punchUsers = new ArrayList<>();
        for (UserInfo info : userInfos) {
            UserInfo userInfo = userInfoManageService.getUserInfoFromUserId(info.getUserId());
            if (userInfo == null) {
                notExistUsers.add(info);
            } else if (!userInfo.getStatus().isOnline()) {
                offloadUsers.add(info);
            } else {
                punchUsers.add(userInfo);
            }
        }
        if (notExistUsers.size() > 0) {
            UserExceptionResponse response = responsePool.createUserExceptionResponse(punchMessage.getRequest().getSocketAddress());
            response.setUserInfos(notExistUsers);
            response.setType(ResponseType.USER_NOT_EXIST);
            responseSender.send(response);
        }
        if (offloadUsers.size() > 0) {
            UserExceptionResponse response = responsePool.createUserExceptionResponse(punchMessage.getRequest().getSocketAddress());
            response.setUserInfos(offloadUsers);
            response.setType(ResponseType.USER_OFFLINE);
            responseSender.send(response);
        }
        if (punchUsers.size() > 0) {
            PunchResponse response = responsePool.createPunchResponse(punchMessage.getRequest().getSocketAddress());
            response.setUserInfos(punchUsers);
            responseSender.send(response);

            List<UserInfo> userInfo = new ArrayList<>(1);
            userInfo.add(user);
            for (UserInfo info : punchUsers) {
                PunchResponse punchResponse = responsePool.createPunchResponse(info.getSocketAddress());
                punchResponse.setUserInfos(userInfo);
                responseSender.send(punchResponse);
            }
        }
    }
}
