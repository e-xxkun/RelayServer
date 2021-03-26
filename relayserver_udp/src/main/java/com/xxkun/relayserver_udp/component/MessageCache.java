package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UClient;
import com.xxkun.relayserver_udp.dao.UDPField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

@Component
public class MessageCache {
    @Autowired
    private OnMsgTimeout onMsgTimeout;

    private final DelayQueue<UDPField> delayQueue;

    private final ConcurrentHashMap<InetSocketAddress, UClient> clientMap;

    private final MsgTimeoutListenThread timeoutListenThread;

    public MessageCache() {
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

    public void ack(UDPField udpMsg) {
        UClient client = clientMap.get(udpMsg.getSocketAddress());
        if (client != null) {
//            delayQueue.remove(message);
            boolean res = client.removeMessage(udpMsg);
            if (client.msgSize() == 0) {
                clientMap.remove(client.getSocketAddress());
            }
        }
    }

    public boolean isTimeout(UDPField udpMsg) {
        UClient client = clientMap.get(udpMsg.getSocketAddress());
        if (client != null) {
            return client.containsMessage(udpMsg);
        }
        return true;
    }

    public void close() {
        timeoutListenThread.close();
    }

    public class MsgTimeoutListenThread extends BaseThread {
        @Override
        public void run() {
            while (stop) {
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
