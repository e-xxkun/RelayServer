package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.common.utils.MessageUtil;
import com.xxkun.relayserver_udp.dao.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
            byte[] inBuff = new byte[Message.UDP_MSG_IN_BUFF_LEN];
            DatagramPacket packet = new DatagramPacket(inBuff, inBuff.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            msgReceiveThreadPool.execute(() -> {
                Message msg = MessageUtil.decodeMessage(packet.getData());
                if (msg == null) {
                    System.out.println("Invalid message from " + packet.getSocketAddress() + ":" + new String(packet.getData()));
                }
                onMessage.onMessage(packet.getSocketAddress(), msg);
            });
        }
    }

    public interface OnMessage {
        void onMessage(SocketAddress from, Message msg);
    }
}
