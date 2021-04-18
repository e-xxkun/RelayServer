package com.xxkun.udptransfer;

import java.io.IOException;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        TransferServer server = null;
        try {
            server = new TransferServer(8888);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                TransferPacket packet = server.receive();
                System.out.println(packet.getSequence());
                System.out.println(packet.getReceiveTime());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
