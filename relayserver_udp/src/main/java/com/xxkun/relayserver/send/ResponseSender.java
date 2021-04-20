package com.xxkun.relayserver.send;

import com.xxkun.relayserver.pojo.response.Response;
import com.xxkun.udptransfer.TransferPacket;
import com.xxkun.udptransfer.TransferServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseSender implements TransferServer.OnPacketReachMaxResendTime {

    @Autowired
    private TransferServer sender;

    public void send(Response response) {
        try {
            TransferPacket packet = new TransferPacket(response.getBodyBuffer(), response.getSocketAddress());
            sender.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (response.hashNext()) {
            send(response.next());
        }
    }

    @Override
    public void onPacketReachMaxResendTime(TransferPacket packet) {

    }
}
