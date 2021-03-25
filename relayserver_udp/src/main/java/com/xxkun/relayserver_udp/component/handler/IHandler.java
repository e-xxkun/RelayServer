package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.dao.UMessage;

public interface IHandler {

    void consume(UMessage msg);
}
