package com.xxkun.udptransfer;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicLong;

public class PacketPool {

    private final DelayQueue<TransferPacket> packetQueue;
    private final Map<InetSocketAddress, Client> clientMap;
    private final TimeoutListenThread timeoutListenThread;
    private OnPacketConfirmTimeout onPacketConfirmTimeout;

    public PacketPool() {
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
        Client client = clientMap.get(packet.getSocketAddress());
        if (client == null) {
            client = new Client(packet.getSocketAddress());
            clientMap.put(packet.getSocketAddress(), client);
        }
        if (!packet.isResend()) {
            packet.setSequence(client.getCurSequence());
        }
        client.addPacket(packet);
        packetQueue.add(packet);
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

    public TransferPacket createACKPacket(TransferPacket packet) {
        byte[] data = new byte[TransferPacket.HEAD_LEN];
        TransferPacket ackPacket = new TransferPacket(data, packet.getSocketAddress(), true);
        ackPacket.setSequence(packet.getSequence());
        return ackPacket;
    }

    public interface OnPacketConfirmTimeout {
        void onPacketConfirmTimeout(TransferPacket packet);
    }

    private static class Client {

        private static final long MAX_RTO = 60 * 1000L;
        private static final long MIN_RTO = 200L;
        private Long SRTT;
        private Long DevRTT;
        private long RTO = 1000L;

        private final Map<TransferPacket, TransferPacket> packetMap;
        private final InetSocketAddress socketAddress;
        private final AtomicLong curSequence;

        public Client(InetSocketAddress socketAddress) {
            this.socketAddress = socketAddress;
            this.curSequence = new AtomicLong(new Random().nextInt(256));
            packetMap = new ConcurrentHashMap<>();
        }

        public void addPacket(TransferPacket packet) {
            packet.setRTO(RTO);
            packetMap.put(packet, packet);
        }

        public void removePacket(TransferPacket packet) {
            TransferPacket srcPacket = packetMap.remove(packet);
            if (srcPacket != null) {
                if (packet.getReceiveTime() == null) {
                    packetTimeout();
                } else {
                    packet.setSendTime(srcPacket.getSendTime());
                    updateRtt(packet);
                }
            }
        }

//        https://www.cnblogs.com/lshs/p/6038535.html
//        alpha = 1/8  beta = 1/4  K = 4
//        DevRTT = (1-beta) * DevRTT + beta *(|RTT - SRTT|)
//        SRTT = (1 - alpha) * SRTT + alpha * (RTT - SRTT);
//        RTO = SRTT + K * DevRTT
        private void updateRtt(TransferPacket packet) {
            long RTT = packet.getSendTime() - packet.getReceiveTime();
            if (SRTT == null) {
                SRTT = RTT;
                DevRTT = RTT >> 1;
            } else {
                DevRTT = DevRTT - (DevRTT >> 2) + (Math.abs(RTT - SRTT) >> 2);
                SRTT = SRTT - (SRTT >> 3) + ((RTT - SRTT) >> 3);
            }
            RTO = SRTT + (DevRTT << 2);
            RTO = Math.max(RTO, MIN_RTO);
            RTO = Math.min(RTO, MAX_RTO);
        }

//        https://www.cnblogs.com/lshs/p/6038535.html
        private void packetTimeout() {
            SRTT = null;
            DevRTT = null;
            RTO <<= 1;
            RTO = Math.min(RTO, MAX_RTO);
            RTO = Math.max(RTO, 3 * 1000L);
        }

        public boolean containsPacket(TransferPacket packet) {
            return packetMap.containsKey(packet);
        }

        public long getCurSequence() {
            return curSequence.getAndIncrement();
        }

        public int packetCount() {
            return packetMap.size();
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
                    Client client = clientMap.get(packet.getSocketAddress());
                    if (client != null && !hasConfirmed(packet)) {
                        client.removePacket(packet);
                        if (onPacketConfirmTimeout != null) {
                            onPacketConfirmTimeout.onPacketConfirmTimeout(packet);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(40);
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
