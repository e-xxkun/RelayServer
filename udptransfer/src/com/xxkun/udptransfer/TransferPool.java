package com.xxkun.udptransfer;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicLong;

public class TransferPool {

    private final DelayQueue<TransferPacket> packetQueue;
    private final Map<InetSocketAddress, Client> clientMap;
    private final TimeoutListenThread timeoutListenThread;
    private OnPacketConfirmTimeout onPacketConfirmTimeout;

    public TransferPool() {
        packetQueue = new DelayQueue<>();
        clientMap = new ConcurrentHashMap<>();
        timeoutListenThread = new TimeoutListenThread();
        timeoutListenThread.start();
    }

    public DatagramPacket createPacket() {
        byte[] data = new byte[TransferServer.MAX_TRANSFER_LEN];
        return new DatagramPacket(data, data.length);
    }

    public void addUnconfirmedPacket(TransferPacket packet) {
        packetQueue.add(packet);
        Client client = clientMap.get(packet.getSocketAddress());
        if (client == null) {
            client = new Client(packet.getSocketAddress());
            clientMap.put(packet.getSocketAddress(), client);
        }
        if (!packet.isResend()) {
            packet.setSequence(client.getCurSequence());
        }
        client.addPacket(packet);
    }

    public void confirm(TransferPacket packet) {
        Client client = clientMap.get(packet.getSocketAddress());
        if (client != null) {
//            packetQueue.remove(response);
            client.removePacket(packet);
            if (client.packetCount() == 0) {
                clientMap.remove(client.getSocketAddress());
            }
        }
    }

    private boolean hasConfirmed(TransferPacket packet) {
        Client client = clientMap.get(packet.getSocketAddress());
        if (client != null) {
            return client.containsPacket(packet);
        }
        return true;
    }

    public void close() {
        timeoutListenThread.close();
        packetQueue.clear();
    }

    public void setOnPacketConfirmTimeout(OnPacketConfirmTimeout onPacketConfirmTimeout) {
        this.onPacketConfirmTimeout = onPacketConfirmTimeout;
    }

    public interface OnPacketConfirmTimeout {
        void onPacketConfirmTimeout(TransferPacket packet);
    }

    private static class Client {

        private final Set<TransferPacket> packetSet;
        private final InetSocketAddress socketAddress;
        private final AtomicLong curSequence;

        public Client(InetSocketAddress socketAddress) {
            this.socketAddress = socketAddress;
            this.curSequence = new AtomicLong(new Random().nextLong() % 256);
            packetSet = ConcurrentHashMap.newKeySet();
        }

        public void addPacket(TransferPacket packet) {
            packetSet.add(packet);
        }

        public void removePacket(TransferPacket packet) {
            packetSet.remove(packet);
        }

        public boolean containsPacket(TransferPacket packet) {
            return packetSet.contains(packet);
        }

        public long getCurSequence() {
            return curSequence.getAndIncrement();
        }

        public int packetCount() {
            return packetSet.size();
        }

        public InetSocketAddress getSocketAddress() {
            return socketAddress;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Client client = (Client) o;
            return socketAddress.equals(client.socketAddress);
        }

        @Override
        public int hashCode() {
            return socketAddress.hashCode();
        }
    }

    private class TimeoutListenThread extends Thread {

        private boolean stop = false;

        @Override
        public void run() {
            while (!stop) {
                try {
                    TransferPacket packet = packetQueue.take();
//                    System.out.println("DELAY: " + response);
                    if (onPacketConfirmTimeout != null && !hasConfirmed(packet)) {
                        onPacketConfirmTimeout.onPacketConfirmTimeout(packet);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        public void close() {
            stop = true;
        }
    }
}
