package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.common.utils.MessageUtil;
import com.xxkun.relayserver_udp.dao.UDPField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Component
public class UDPMessageSender implements MessageCache.OnMsgTimeout{

    @Autowired
    private DatagramSocket socket;
    @Autowired
    private MessageCache messageCache;

    public void send(UDPField udpField) {
        try {
            messageCache.addToCache(udpField);
            DatagramPacket packet = MessageUtil.decodeUDPField(udpField);
            if (packet == null) {
                return;
            }
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            resend(udpField);
        }
    }

    @Override
    public void onMsgTimeout(UDPField msg) {
        resend(msg);
    }

    private void resend(UDPField udpField) {
        if (udpField.isMaxResendTime()) {
            return;
        }
        udpField.incResendTime();
        send(udpField);
    }
}
