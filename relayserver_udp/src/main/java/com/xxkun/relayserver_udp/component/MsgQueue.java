package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UMessage;

import java.util.concurrent.LinkedBlockingQueue;

public class MsgQueue implements IMsgQueue {

    private LinkedBlockingQueue<UMessage> queue = new LinkedBlockingQueue<>();

    @Override
    public void sendMessage(UMessage msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
