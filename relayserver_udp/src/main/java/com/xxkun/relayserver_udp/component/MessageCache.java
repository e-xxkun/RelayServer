package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UClient;
import com.xxkun.relayserver_udp.dao.UDPField;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

public class MessageCache {

    private final DelayQueue<UDPField> delayQueue;

    private final ConcurrentHashMap<InetSocketAddress, UClient> clientMap;

    private MsgTimeoutListenThread timeoutListenThread;

    private OnMsgTimeout onMsgTimeout;

    public MessageCache(OnMsgTimeout onMsgTimeout) {
        this.onMsgTimeout = onMsgTimeout;
        delayQueue = new DelayQueue<>();
        clientMap = new ConcurrentHashMap<>();
        timeoutListenThread = new MsgTimeoutListenThread();
        timeoutListenThread.start();
    }

    public void addToCache(UDPField udpMsg) {
        delayQueue.add(udpMsg);
        UClient client = clientMap.get(udpMsg.getSocketAddress());
        if (client == null) {
            client = new UClient(udpMsg.getSocketAddress());
            clientMap.put(udpMsg.getSocketAddress(), client);
        }
        if (!udpMsg.isResend()) {
            udpMsg.setSeq(client.getCurSeq());
        }
        client.addUDPMsg(udpMsg);
    }

    public boolean ack(UDPField udpMsg) {
        UClient client = clientMap.get(udpMsg.getSocketAddress());
        if (client != null) {
//            delayQueue.remove(message);
            boolean res = client.removeMessage(udpMsg);
            if (client.msgSize() == 0) {
                clientMap.remove(client.getSocketAddress());
            }
            return res;
        }
        return false;
    }

    public boolean isTimeout(UDPField udpMsg) {
        UClient client = clientMap.get(udpMsg.getSocketAddress());
        if (client != null) {
            return client.containsMessage(udpMsg);
        }
        return true;
    }

    public class MsgTimeoutListenThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    UDPField msg = delayQueue.take();
                    if (isTimeout(msg)) {
                        onMsgTimeout.onMsgTimeout(msg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public interface OnMsgTimeout {
        void onMsgTimeout(UDPField msg);
    }
}
