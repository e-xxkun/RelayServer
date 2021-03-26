package com.xxkun.relayserver_udp.component;


import com.xxkun.relayserver_udp.UDPReceiveLoopThread;
import com.xxkun.relayserver_udp.dao.UMessage;
import com.xxkun.relayserver_udp.dao.UserSession;
import com.xxkun.relayserver_udp.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketAddress;


public class MessageReceiveHandler implements UDPReceiveLoopThread.OnMessage {

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
    }
}
