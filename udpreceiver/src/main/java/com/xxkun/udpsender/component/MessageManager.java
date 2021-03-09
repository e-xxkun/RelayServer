package com.xxkun.udpsender.component;


import com.xxkun.udpsender.UDPReceiveLoopThread;
import com.xxkun.udpsender.dao.UMessage;
import com.xxkun.udpsender.dao.UserSession;
import com.xxkun.udpsender.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;

public class MessageManager implements UDPReceiveLoopThread.OnMessage {

    @Autowired
    private UserInfoManageService userInfoManageService;

    @Autowired
    private MsgQueueSender msgQueueSender;

    @Override
    public void onMessage(SocketAddress from, UMessage msg) {
        UserSession userSession = userInfoManageService.getUserSessionFromToken(msg.getToken());
        if (userSession == null) {
            return;
        }
//        userInfoManageService.updateUserSession(userSession);
//        UserIdentifier userIdentifier = userInfoManageService.updateUserInfo(userSession);
//        msg.setIdentifier(userIdentifier);

        switch (msg.getType()) {

            case MSGT_HEARTBEAT:
                msgQueueSender.sendHeartbeatMessage(msg);
                break;
            case MSGT_REPLY:
                break;
            case MSGT_TEXT:
                break;
            case MSGT_UNKNOWN:
                break;
            case NPT_FULL_OR_RESTRICTED_CONE_NAT:
                break;
            case NPT_SYMMETRIC_NAT:
                break;
            case NPT_START:
                break;
            case NPT_STEP_1:
                break;
            case NPT_STEP_2:
                break;
        }
    }
}
