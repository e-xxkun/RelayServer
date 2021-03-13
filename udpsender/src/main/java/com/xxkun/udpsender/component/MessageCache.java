package com.xxkun.udpsender.component;

import com.xxkun.udpsender.dao.Message;

import java.util.HashSet;
import java.util.concurrent.DelayQueue;

public class MessageCache {

    private final DelayQueue<Message> delayQueue;

    private final HashSet<Message> msgSet;

    private UDPMessageSender messageSender;

    private MsgTimeoutListenThread timeoutListenThread;

    public MessageCache(DelayQueue<Message> delayQueue, HashSet<Message> msgSet) {
        this.delayQueue = delayQueue;
        this.msgSet = msgSet;
        timeoutListenThread = new MsgTimeoutListenThread();
        timeoutListenThread.start();
    }

    public boolean addToCache(Message message) {
        delayQueue.add(message);
        msgSet.add(message);
        return true;
    }

    public boolean ack(Message message) {
        if (msgSet.contains(message)) {
            msgSet.remove(message);
//            delayQueue.remove(message);
            return true;
        }
        return false;
    }

    public boolean isTimeout(Message message) {
        return msgSet.contains(message);
    }

    private void resendMsg(Message message) {
        messageSender.send(message.convertToUDPField());
    }

    public class MsgTimeoutListenThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Message msg = delayQueue.take();
                    if (isTimeout(msg)) {
                        resendMsg(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
