package com.xxkun.udptransfer;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class TransferPacket implements Delayed {
    private static final int HEAD = 0xFEFDDFEB;
    // HEAD|sequence|type|bodyLength -> int|long|byte|int
    private static final int HEAD_LEN = Integer.BYTES + Long.BYTES + Byte.BYTES + Integer.BYTES;
    private static int MAX_RESEND_TIME = 4;

    private Long sequence;
    private int resendTime = 0;
    private final Type type;
    private InetSocketAddress socketAddress;
    private int bodyLength = TransferServer.MAX_TRANSFER_LEN - HEAD_LEN;

    private String id;
    private long rtt;
    private long receiveTime;
    private long sendTime;

    private final ByteBuffer buffer;

    public TransferPacket(byte[] data, InetSocketAddress socketAddress) {
        this(data, socketAddress, false);
    }
    
    public TransferPacket(byte[] data, InetSocketAddress socketAddress, boolean isACK) {
        this(data, socketAddress, isACK ? Type.ACK : Type.GET);
    }

    public TransferPacket(byte[] data, InetSocketAddress socketAddress, Type type) {
        this.socketAddress = socketAddress;
        buffer = ByteBuffer.wrap(data);
        this.type = type;
    }

    public byte[] convertToByteArray() {
        buffer.position(0);
        buffer.putInt(HEAD);
        buffer.putLong(sequence);
        buffer.put(type.getCode());
        buffer.putInt(getBodyLength());
        return buffer.array();
    }

    public DatagramPacket getDatagramPacket() {
        return new DatagramPacket(convertToByteArray(), getBodyLength(), socketAddress);
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public boolean isACK() {
        return type.isACK();
    }

    public boolean isMaxResendTime() {
        return resendTime >= MAX_RESEND_TIME;
    }

    public void incResendTime() {
        ++ resendTime;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        refreshId();
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public boolean isResend() {
        return false;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
        refreshId();
    }

    private void refreshId() {
        id = socketAddress.toString();
        if (sequence != null) {
            id += sequence;
        }
    }

    public void setRtt(long rtt) {
        this.rtt = rtt;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long time) {
        this.receiveTime = time;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long time) {
        this.sendTime = time;
    }


    public static void setMaxResendTime(int maxResendTime) {
        MAX_RESEND_TIME = maxResendTime;
    }


    public static TransferPacket decodeFromDatagramPacket(DatagramPacket packet) {
        return null;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return rtt;
    }

    @Override
    public int compareTo(Delayed o) {
        return (sendTime - ((TransferPacket) o).sendTime
                + getDelay(TimeUnit.SECONDS) -  o.getDelay(TimeUnit.SECONDS)) <= 0 ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferPacket packet = (TransferPacket) o;
        return id.equals(packet.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private enum Type {
        ACK(0) {
            @Override
            public boolean isACK() {
                return true;
            }
        },
        GET(1);

        private final int code;

        Type(int code) {
            this.code = code;
        }

        public byte getCode() {
            return (byte) code;
        }

        public boolean isACK() {
            return false;
        }
    }
}
