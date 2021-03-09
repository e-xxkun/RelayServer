package com.xxkun.udpsender.component;

import com.xxkun.udpsender.dao.UMessage;
import com.xxkun.udpsender.dto.QueueEnum;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgQueueSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendHeartbeatMessage(UMessage uMessage){
        amqpTemplate.convertAndSend(QueueEnum.HEARTBEAT.getExchange(), QueueEnum.HEARTBEAT.getRouteKey(), uMessage);
    }
}
