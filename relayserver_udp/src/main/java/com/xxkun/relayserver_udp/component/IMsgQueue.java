package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.dao.UMessage;

public interface IMsgQueue {
    void sendMessage(UMessage msg);
}
