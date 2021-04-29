package com.xxkun.udptransfer;

import java.io.IOException;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TransferServer implements PacketPool.OnPacketConfirmTimeout {

    public final static int MAX_TRANSFER_LEN = 512;

    private DatagramSocket socket;
    private PacketPool pool;
    private Set<InetSocketAddress> whiteList;

    private boolean interceptAll = true;

    private OnPacketReachMaxResendTime onPacketReachMaxResendTime;
    private OnPacketReceive onPacketReceive;

    public TransferServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
        init();
    }

    public SocketAddress getLocalSocketAddress() {
        return socket.getLocalSocketAddress();
    }

    public TransferServer() throws SocketException {
        socket = new DatagramSocket();
        init();
    }

    private void init() {
        whiteList = ConcurrentHashMap.newKeySet();
        pool = new PacketPool();
        pool.setOnPacketConfirmTimeout(this);
    }

    public void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    public void send(TransferPacket packet) throws IOException {
        send(packet, true);
    }

    private void send(TransferPacket packet, boolean needACK) throws IOException {
        if (needACK) {
            packet.setSendTime(System.currentTimeMillis());
            pool.addUnconfirmedPacket(packet);
        }
        DatagramPacket datagramPacket = new DatagramPacket(packet.convertToByteArray(), packet.length(), packet.getSocketAddress());
        socket.send(datagramPacket);
    }

    public TransferPacket receive() throws IOException {
        DatagramPacket packet = pool.createPacket();
        while (true) {
            socket.receive(packet);
            InetSocketAddress socketAddress = (InetSocketAddress) packet.getSocketAddress();
            if (interceptAll || whiteList.contains(socketAddress)) {
                TransferPacket transferPacket = TransferPacket.decodeFromByteArray(packet.getData(), socketAddress);
                if (transferPacket == null) {
                    continue;
                }
                transferPacket.setReceiveTime(System.currentTimeMillis());
                if (transferPacket.isACK()) {
                    pool.confirm(transferPacket);
                } else {
                    sendACK(transferPacket);
                    return transferPacket;
                }
            } else if (onPacketReceive != null){
                onPacketReceive.onPacketReceive(packet);
            }
        }
    }

    private void sendACK(TransferPacket packet) throws IOException {
        TransferPacket ackPacket = pool.createACKPacket(packet);
        send(ackPacket, false);
    }

    public void addReceiveWhiteList(Set<InetSocketAddress> whiteList) {
        this.whiteList.addAll(whiteList);
    }

    public Set<InetSocketAddress> getReceiveWhiteList() {
        return whiteList;
    }

    public boolean isInterceptAll() {
        return interceptAll;
    }

    public void setInterceptAll(boolean interceptAll) {
        this.interceptAll = interceptAll;
    }

    @Override
    public void onPacketConfirmTimeout(TransferPacket packet) {
        if (packet.isMaxResendTime()) {
            if (onPacketReachMaxResendTime != null) {
                onPacketReachMaxResendTime.onPacketReachMaxResendTime(packet);
            }
            return;
        }
        packet.incResendTime();
        try {
            send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnPacketReachMaxResendTime(OnPacketReachMaxResendTime onPacketReachMaxResendTime) {
        this.onPacketReachMaxResendTime = onPacketReachMaxResendTime;
    }

    public void setOnPacketReceive(OnPacketReceive onPacketReceive) {
        this.onPacketReceive = onPacketReceive;
    }

    public interface OnPacketReachMaxResendTime {
        void onPacketReachMaxResendTime(TransferPacket packet);
    }

    public interface OnPacketReceive {
        void onPacketReceive(DatagramPacket packet);
    }
}
