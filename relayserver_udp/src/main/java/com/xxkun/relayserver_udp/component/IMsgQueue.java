package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.Message;

public interface IMsgQueue {

    boolean sendMessage(Message msg);

    interface OnMessage {
        void onMessage(Message msg);
    }
}
