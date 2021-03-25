package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MsgQueue implements IMsgQueue {

    @Autowired
    private OnMessage onMessage;

    private final LinkedBlockingQueue<UMessage> queue;

    private final MsgListenThread msgListenThread;


    public MsgQueue() {
        queue = new LinkedBlockingQueue<>();
        msgListenThread = new MsgListenThread();
        msgListenThread.start();
    }

    @Override
    public boolean sendMessage(UMessage msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        msgListenThread.close();
    }

    class MsgListenThread extends Thread {

        private boolean stop = false;

        @Override
        public void run() {
            while (!stop) {
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

        public void close() {
            stop = true;

        }
    }
}
