package com.xxkun.udpsender.component;

import com.xxkun.udpsender.dao.Message;
import com.xxkun.udpsender.dao.UClient;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

public class MessageCache {

    private final DelayQueue<Message> delayQueue;

    private final ConcurrentHashMap<InetSocketAddress, UClient> clientMap;

    private UDPMessageSender messageSender;

    private MsgTimeoutListenThread timeoutListenThread;

    public MessageCache(DelayQueue<Message> delayQueue, ConcurrentHashMap<InetSocketAddress, UClient> clientMap) {
        this.delayQueue = delayQueue;
        this.clientMap = clientMap;
        timeoutListenThread = new MsgTimeoutListenThread();
        timeoutListenThread.start();
    }

    public boolean addToCache(Message message) {
        delayQueue.add(message);
        UClient client = clientMap.get(message.getSocketAddress());
        if (client == null) {
            client = new UClient(message.getSocketAddress());
            clientMap.put(message.getSocketAddress(), client);
        }
        client.addMessage(message);
        return true;
    }

    public boolean ack(Message message) {
        UClient client = clientMap.get(message.getSocketAddress());
        if (client != null) {
//            delayQueue.remove(message);
            boolean res = client.removeMessage(message);
            if (client.msgSize() == 0) {
                clientMap.remove(client.getSocketAddress());
            }
            return res;
        }
        return false;
    }

    public long getCurSeqOfClient(Message message) {
        delayQueue.add(message);
        UClient client = clientMap.get(message.getSocketAddress());
        if (client == null) {
            client = new UClient(message.getSocketAddress());
            clientMap.put(message.getSocketAddress(), client);
        }
        return client.getCurSeq();
    }

    public boolean isTimeout(Message message) {
        UClient client = clientMap.get(message.getSocketAddress());
        if (client != null) {
            return client.containsMessage(message);
        }
        return true;
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
