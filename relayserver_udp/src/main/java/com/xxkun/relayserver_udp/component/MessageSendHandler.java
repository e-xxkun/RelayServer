package com.xxkun.relayserver_udp.component;

import com.xxkun.relayserver_udp.component.handler.HeartbeatMsgHandler;
import com.xxkun.relayserver_udp.dao.UMessage;
import com.xxkun.relayserver_udp.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MessageSendHandler implements IMsgQueue.OnMessage{

    @Autowired
    private HeartbeatMsgHandler heartbeatMsgHandler;

    @Override
    public void onMessage(UMessage msg) {
        switch (msg.getType()) {
            case MSGT_HEARTBEAT:
                heartbeatMsgHandler.consume(msg);
                break;
            case MSGT_REPLY:
                break;
            case MSGT_TEXT:
                break;
            case MSGT_UNKNOWN:
                break;
            case NPT_FULL_OR_RESTRICTED_CONE_NAT:
                break;
            case NPT_SYMMETRIC_NAT:
                break;
            case NPT_START:
                break;
            case NPT_STEP_1:
                break;
            case NPT_STEP_2:
                break;
        }
    }
}
