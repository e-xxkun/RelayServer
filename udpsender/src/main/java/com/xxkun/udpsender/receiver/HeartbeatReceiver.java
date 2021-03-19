package com.xxkun.udpsender.receiver;

import com.xxkun.udpsender.common.utils.ConstantField;
import com.xxkun.udpsender.dao.UMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@RabbitListener(queues = ConstantField.QUEUE_HEARTBEAT_NAME)
public class HeartbeatReceiver {

    @RabbitHandler
    public void handle(UMessage uMessage) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        serverSocket.accept();
        Socket socket = new Socket();
    }

}
