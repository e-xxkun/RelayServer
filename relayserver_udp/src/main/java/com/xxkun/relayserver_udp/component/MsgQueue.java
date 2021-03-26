package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.Message;
import com.xxkun.relayserver_udp.dao.UMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MsgQueue implements IMsgQueue {
    @Autowired
    private OnMessage onMessage;
    @Resource(name = "msgSendThreadPool")
    private ThreadPoolExecutor msgQueueThreadPool;

    private final LinkedBlockingQueue<Message> queue;

    private final MsgListenThread msgListenThread;

    public MsgQueue() {
        queue = new LinkedBlockingQueue<>();
        msgListenThread = new MsgListenThread();
        msgListenThread.start();
    }

    @Override
    public boolean sendMessage(Message msg) {
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
        if (!msgQueueThreadPool.isShutdown()) {
            msgQueueThreadPool.shutdown();
        }
    }

    class MsgListenThread extends BaseThread {
        @Override
        public void run() {
            while (!stop) {
                Message message = null;
                try {
                    message = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (message != null) {
                    Message finalMessage = message;
                    msgQueueThreadPool.execute(() -> onMessage.onMessage(finalMessage));
                }
            }
        }
    }
}
