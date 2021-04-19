package com.xxkun.relayserver.receive;

import com.xxkun.relayserver.component.BaseThread;
import com.xxkun.relayserver.component.exception.RequestResolutionException;
import com.xxkun.relayserver.pojo.request.Request;
import com.xxkun.udptransfer.TransferPacket;
import com.xxkun.udptransfer.TransferServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class RequestListener extends BaseThread implements ApplicationRunner {
    @Autowired
    private TransferServer receiver;
    @Autowired
    private OnRequest onRequest;
    @Resource(name = "requestThreadPool")
    private ThreadPoolExecutor requestThreadPool;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        start();
    }

    @Override
    public void run() {
        while (!stop) {
            System.out.println("LISTEN START");
            TransferPacket packet;
            try {
                packet = receiver.receive();
                System.out.println("RECEIVE: from" + packet.getSocketAddress());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            requestThreadPool.execute(() -> {
                try {
                    Request request = Request.decodeFromByteArray(packet.getBuffer(), packet.getSocketAddress());
                    onRequest.onRequest(packet.getSocketAddress(), request);
                } catch (RequestResolutionException e) {
                    System.out.println("Invalid message from " + packet.getSocketAddress() + ":" + new String(packet.convertToByteArray()));
                }
            });
        }
    }


    public interface OnRequest {
        void onRequest(SocketAddress from, Request request);
    }
}
