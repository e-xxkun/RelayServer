package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.dao.FriendInfo;
import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.request.Message;
import com.xxkun.relayserver.dao.request.message.PunchMessage;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PunchMessageHandler implements IMessageHandler{
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Override
    public void consume(Message message) {
        PunchMessage punchMessage = (PunchMessage) message;
        FriendInfo[] friendInfos = punchMessage.getFriendInfos();
        List<FriendInfo> notExistUsers = new ArrayList<>();
        List<FriendInfo> offloadUsers = new ArrayList<>();
        List<UserInfo> userInfos = new ArrayList<>();
        for (FriendInfo info : friendInfos) {
            UserInfo userInfo = userInfoManageService.getUserInfoFromUserId(info.getUserId());
            if (userInfo == null) {
                notExistUsers.add(info);
            } else if (userInfo.isOffload()) {
                offloadUsers.add(info);
            } else {
                userInfos.add(userInfo);
            }
        }

    }
}
