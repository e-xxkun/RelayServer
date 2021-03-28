package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.component.handler.ACKMsgHandler;
import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UDPField;
import com.xxkun.relayserver_udp.dao.UserSession;
import com.xxkun.relayserver_udp.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

@Component
public class MessageReceiveHandler implements MessageListener.OnMessage {

    @Autowired
    private UserInfoManageService userInfoManageService;
    @Autowired
    private ACKMsgHandler ackMsgHandler;
    @Autowired
    private IMsgQueue msgQueueSender;

    @Override
    public void onMessage(SocketAddress from, UDPField udpField) {
        if (udpField.isACK()) {
//            ackMsgHandler.consume(udpField);
            return;
        }
        Message msg = Message.decodeFromUDPField(udpField);
        if (msg == null) {
            return;
        }
        UserSession userSession = userInfoManageService.getUserSessionFromToken(msg.getToken());
        if (userSession == null) {
            return;
        }
        msgQueueSender.sendMessage(msg);
    }
}
