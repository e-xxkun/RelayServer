package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.dao.Message;

public interface IMessageHandler {

    void consume(Message msg);
}
