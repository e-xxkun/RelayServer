package com.xxkun.relayserver_udp;

import com.xxkun.relayserver_udp.dao.UMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class UDPReceiveLoopThread extends Thread {
    private final DatagramSocket socket;
    private final OnMessage onMessage;

    public UDPReceiveLoopThread(DatagramSocket socket, OnMessage onMessage) {
        this.socket = socket;
        this.onMessage = onMessage;
    }

    @Override
    public void run() {
        while (true) {
            byte[] inBuff = new byte[UMessage.UDP_MSG_IN_BUFF_LEN];
            DatagramPacket packet = new DatagramPacket(inBuff, inBuff.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
//                e.printStackTrace();
                break;
            }
            String inDataStr = new String(packet.getData());
            UMessage msg = UMessage.msgUnpack(inDataStr);
            if (msg == null) {
                System.out.println("Invalid message from " + packet.getSocketAddress() + ":" + inDataStr);
                continue;
            }
            onMessage.onMessage(packet.getSocketAddress(), msg);
        }
    }

    public interface OnMessage {
        void onMessage(SocketAddress from, UMessage msg);
    }
}
