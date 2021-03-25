package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.UDPReceiveLoopThread;
import com.xxkun.relayserver_udp.dao.UMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MsgQueue implements IMsgQueue {

    @Autowired
    private OnMessage onMessage;

    private LinkedBlockingQueue<UMessage> queue;
    private MsgListenThread msgListenThread;

    public MsgQueue() {
        queue = new LinkedBlockingQueue<>();
        msgListenThread = new MsgListenThread();
        msgListenThread.start();
    }

    @Override
    public void sendMessage(UMessage msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class MsgListenThread extends Thread {
        @Override
        public void run() {
            while (true) {
                UMessage message = null;
                try {
                    message = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (message != null) {
                    onMessage.onMessage(message);
                }
            }
        }
    }
}
