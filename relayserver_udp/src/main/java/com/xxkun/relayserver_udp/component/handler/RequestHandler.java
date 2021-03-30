package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.component.queue.IMsgQueue;
import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dao.UserSession;
import com.xxkun.relayserver_udp.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RequestHandler implements IUDPFieldHandler {
    @Autowired
    private UserInfoManageService userInfoManageService;
    @Qualifier("putMsgQueue")
    @Autowired
    private IMsgQueue putMsgQueueSender;
    @Qualifier("getMsgQueue")
    @Autowired
    private IMsgQueue getMsgQueueSender;
    
    @Override
    public void consume(UDPField udpField) {
        Message msg = Message.decodeFromUDPField(udpField);
        if (msg == null) {
            return;
        }
        UserSession userSession = userInfoManageService.getUserSessionFromToken(msg.getToken());
        if (userSession == null) {
            return;
        }
        if (udpField.getType().isGET()) {
            getMsgQueueSender.sendMessage(msg);
        } else {
            putMsgQueueSender.sendMessage(msg);
        }
    }
}
