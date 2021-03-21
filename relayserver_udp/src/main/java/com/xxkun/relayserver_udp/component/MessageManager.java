package com.xxkun.relayserver_udp.component;


import com.xxkun.relayserver_udp.UDPReceiveLoopThread;
import com.xxkun.relayserver_udp.dao.UMessage;
import com.xxkun.relayserver_udp.dao.UserSession;
import com.xxkun.relayserver_udp.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;

public class MessageManager implements UDPReceiveLoopThread.OnMessage {

    @Autowired
    private UserInfoManageService userInfoManageService;

    @Autowired
    private IMsgQueue msgQueueSender;

    @Override
    public void onMessage(SocketAddress from, UMessage msg) {
        UserSession userSession = userInfoManageService.getUserSessionFromToken(msg.getToken());
        if (userSession == null) {
            return;
        }

        msgQueueSender.sendMessage(msg);
//        switch (msg.getType()) {
//            case MSGT_HEARTBEAT:
//        long remainTime = userInfoManageService.getExpire(userSession);
//        if (remainTime < EXPIRE_TIME / 3) {
//            msgQueueSender.sendHeartbeatMessage(msg);
//        }
//        break;
//                break;
//            case MSGT_REPLY:
//                break;
//            case MSGT_TEXT:
//                break;
//            case MSGT_UNKNOWN:
//                break;
//            case NPT_FULL_OR_RESTRICTED_CONE_NAT:
//                break;
//            case NPT_SYMMETRIC_NAT:
//                break;
//            case NPT_START:
//                break;
//            case NPT_STEP_1:
//                break;
//            case NPT_STEP_2:
//                break;
//        }
    }
}
