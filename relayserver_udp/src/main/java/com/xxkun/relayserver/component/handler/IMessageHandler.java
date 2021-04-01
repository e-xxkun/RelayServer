package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.dao.Message;

public interface IMessageHandler {

    void consume(Message message);
}
