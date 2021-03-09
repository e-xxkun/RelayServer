package com.xxkun.udpsender.receiver;

import com.xxkun.udpsender.common.utils.ConstantField;
import com.xxkun.udpsender.dao.UMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = ConstantField.QUEUE_HEARTBEAT_NAME)
public class HeartbeatReceiver {

    @RabbitHandler
    public void handle(UMessage uMessage){
    }

}
