package com.xxkun.udptransfer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class TransferPacket implements Delayed {

    private static final int HEAD = 0xFEFDDFEB;
    // HEAD|sequence|type|bodyLength -> int|long|byte|int
    public static final int HEAD_LEN = Integer.BYTES + Long.BYTES + Byte.BYTES + Integer.BYTES;
    private static int MAX_RESEND_TIME = 4;

    private Long sequence;
    private int resendTime = 0;
    private InetSocketAddress socketAddress;
    private int bodyLength = 0;
    private final Type type;

    private String id;
    private long RTO;
    private Long receiveTime;
    private Long sendTime;

    private final ByteBuffer buffer;

    public TransferPacket(InetSocketAddress socketAddress) {
        this(new byte[TransferServer.MAX_TRANSFER_LEN], socketAddress, false);
    }

    public TransferPacket(byte[] data, InetSocketAddress socketAddress) {
        this(data, socketAddress, false);
    }

    public TransferPacket(byte[] data, InetSocketAddress socketAddress, boolean isACK) {
        this(data, socketAddress, isACK ? Type.ACK : Type.GET);
    }

    public TransferPacket(byte[] data, InetSocketAddress socketAddress, Type type) {
        this(ByteBuffer.wrap(data), socketAddress, type);
    }

    public TransferPacket(ByteBuffer buffer, InetSocketAddress socketAddress, Type type) {
        this.socketAddress = socketAddress;
        this.buffer = buffer;
        this.type = type;
        refreshId();
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public int length() {
        return HEAD_LEN + bodyLength;
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

    public void setSequence(Long sequence) {
        this.sequence = sequence;
        refreshId();
    }

    public Long getSequence() {
        return sequence;
    }

    public Type getType() {
        return type;
    }

    public ByteBuffer getBuffer() {
        buffer.position(HEAD_LEN);
        return buffer;
    }

    public void setRTO(long RTO) {
        this.RTO = RTO;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long time) {
        this.receiveTime = time;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long time) {
        this.sendTime = time;
    }

    public static void setMaxResendTime(int maxResendTime) {
        MAX_RESEND_TIME = maxResendTime;
    }

    private void refreshId() {
        id = socketAddress.toString();
        if (sequence != null) {
            id += sequence;
        }
    }

    public byte[] convertToByteArray() {
        buffer.position(0);
        buffer.putInt(HEAD);
        buffer.putLong(sequence);
        buffer.put(type.getCode());
        buffer.putInt(getBodyLength());
        return buffer.array();
    }

    public static TransferPacket decodeFromByteArray(byte[] data, InetSocketAddress socketAddress) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        if (buffer.getInt() != HEAD) {
          return null;
        }
        long sequence = buffer.getLong();
        if (sequence < 0) {
            return null;
        }
        int code = buffer.get();
        Type type = Type.fromCode(code);
        int bodyLength = buffer.getInt();
        if (bodyLength < 0) {
            return null;
        }
        TransferPacket packet = new TransferPacket(buffer, socketAddress, type);
        packet.setSequence(sequence);
        packet.setBodyLength(bodyLength);
        return packet;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return RTO;
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

        public static Type fromCode(int code) {
            if (code >= 0 && code < Type.values().length) {
                return Type.values()[code];
            }
            return GET;
        }
    }
}
