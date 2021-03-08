package com.xxkun.udpreceiver.component;

import com.xxkun.udpreceiver.dao.UMessage;
import com.xxkun.udpreceiver.dto.QueueEnum;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
