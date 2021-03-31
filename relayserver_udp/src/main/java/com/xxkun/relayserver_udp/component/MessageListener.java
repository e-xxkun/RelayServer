package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.component.exception.UDPFieldResolutionException;
import com.xxkun.relayserver_udp.dao.UDPField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MessageListener extends BaseThread {
    @Autowired
    private DatagramSocket socket;
    @Autowired
    private OnMessage onMessage;
    @Resource(name = "msgReceiveThreadPool")
    private ThreadPoolExecutor msgReceiveThreadPool;

    @Override
    public void run() {
        while (!stop) {
            byte[] inBuff = new byte[UDPField.UDP_MSG_MAX_LEN];
            DatagramPacket packet = new DatagramPacket(inBuff, inBuff.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            msgReceiveThreadPool.execute(() -> {
                UDPField udpField;
                try {
                    udpField = UDPField.decodeFromByteArray(packet.getData(), (InetSocketAddress)packet.getSocketAddress());
                    onMessage.onMessage(packet.getSocketAddress(), udpField);
                } catch (UDPFieldResolutionException e) {
                    System.out.println("Invalid message from " + packet.getSocketAddress() + ":" + new String(packet.getData()));
                    e.printStackTrace();
                }
            });
        }
    }

    public interface OnMessage {
        void onMessage(SocketAddress from, UDPField udpField);
    }
}
