package com.xxkun.udptransfer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class TransferServer implements TransferPool.OnPacketConfirmTimeout {

    public final static int MAX_TRANSFER_LEN = 512;

    private DatagramSocket socket;

    private TransferPool pool;

    public TransferServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        init();
    }

    public TransferServer() throws SocketException {
        socket = new DatagramSocket();
        init();
    }

    private void init() {
        pool = new TransferPool();
        pool.setOnPacketConfirmTimeout(this);
    }

    public void send(TransferPacket packet) throws IOException {
        send(packet, true);
    }

    public void send(TransferPacket packet, boolean needACK) throws IOException {
        if (needACK) {
            pool.addUnconfirmedPacket(packet);
        }
        socket.send(packet.getDatagramPacket());
    }

    public TransferPacket receive() throws IOException {
        DatagramPacket packet = pool.createPacket();
        while (true) {
            socket.receive(packet);
            TransferPacket transferPacket = TransferPacket.decodeFromDatagramPacket(packet);
            if (transferPacket.isACK()) {
                pool.confirm(transferPacket);
            } else {
                sendACK(transferPacket);
                return transferPacket;
            }
        }
    }

    private void sendACK(TransferPacket transferPacket) {
    }

    @Override
    public void onPacketConfirmTimeout(TransferPacket packet) {
        if (packet.isMaxResendTime()) {
            return;
        }
        packet.incResendTime();
        try {
            send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}