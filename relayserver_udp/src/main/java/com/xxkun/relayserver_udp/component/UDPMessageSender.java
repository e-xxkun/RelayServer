package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.common.utils.MessageUtil;
import com.xxkun.relayserver_udp.dao.UDPField;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPMessageSender implements MessageCache.OnMsgTimeout{

    private final DatagramSocket socket;

    private MessageCache messageCache;

    public UDPMessageSender(DatagramSocket socket) {
        this.socket = socket;
    }

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
