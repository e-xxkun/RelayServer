package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UMessage;
import com.xxkun.relayserver_udp.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSendManager implements IMsgQueue.OnMessage{

    @Autowired
    private UserInfoManageService userInfoManageService;

    @Override
    public void onMessage(UMessage msg) {

    }
}
