package com.xxkun.relayserver.component.handler;

import com.xxkun.relayserver.dao.Request;

public interface IRequestHandler {

    void consume(Request request);
}
