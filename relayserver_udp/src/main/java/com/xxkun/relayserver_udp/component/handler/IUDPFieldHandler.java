package com.xxkun.relayserver_udp.component.handler;

import com.xxkun.relayserver_udp.dao.UDPField;

public interface IUDPFieldHandler {

    void consume(UDPField udpField);
}
