package com.xxkun.relayserver.component;

import com.xxkun.relayserver.dao.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;

@Component
public class ResponseSender implements ResponsePool.OnResponseTimeout {

    @Autowired
    private DatagramSocket sender;
    @Autowired
    private ResponsePool responsePool;

    public void send(Response response) {
        try {
            response.setSendDate(new Date());
            responsePool.add(response);
            byte[] body = response.convertToByteArray();
            DatagramPacket packet = new DatagramPacket(body, body.length, response.getSocketAddress());
            sender.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            resend(response);
        }
    }

    @Override
    public void onResponseTimeout(Response response) {
        resend(response);
    }

    private void resend(Response response) {
        if (response.isMaxResendTime()) {
            return;
        }
        response.incResendTime();
        send(response);
    }
}
