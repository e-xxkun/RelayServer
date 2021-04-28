package com.xxkun.udptransfer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    static TransferServer server = null;

    public static void main(String[] args) {
        try {
            server = new TransferServer();
            new Receiver().start();
            new Sender().start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    static class Receiver extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    TransferPacket packet = server.receive();
                    System.out.println(packet.getSequence());
                    System.out.println(packet.getReceiveTime());
                    System.out.println(packet.getType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Sender extends Thread {
        @Override
        public void run() {
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);
            Scanner in = new Scanner(System.in);
            while (true) {
                try {
                    in.nextLine();
                    TransferPacket packet = new TransferPacket(new TransferPacket.BodyBuffer(),socketAddress);
                    server.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
