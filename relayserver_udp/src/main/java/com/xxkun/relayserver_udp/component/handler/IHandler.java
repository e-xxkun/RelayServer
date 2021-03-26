package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.dao.Message;

public interface IHandler {

    void consume(Message msg);
}
