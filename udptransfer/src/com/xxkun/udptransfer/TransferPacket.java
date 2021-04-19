package com.xxkun.udptransfer;

import java.net.InetSocketAddress;
import java.nio.BufferOverflowException;
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
    private final Type type;

    private String id;
    private long RTO;
    private Long receiveTime;
    private Long sendTime;

    private final BodyBuffer buffer;
    
    public TransferPacket(BodyBuffer buffer, InetSocketAddress socketAddress) {
        this(buffer, socketAddress, Type.GET);
    }

    public TransferPacket(BodyBuffer buffer, InetSocketAddress socketAddress, Type type) {
        this.socketAddress = socketAddress;
        this.buffer = buffer;
        this.type = type;
        refreshId();
    }

    public int getBodyLength() {
        return buffer.getBodyLength();
    }

    public int length() {
        return HEAD_LEN + buffer.getBodyLength();
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

    public BodyBuffer getBuffer() {
        buffer.position(0);
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
        int curPosition = buffer.position();
        buffer.byteBuffer.position(0);
        buffer.byteBuffer.putInt(HEAD);
        buffer.byteBuffer.putLong(sequence);
        buffer.byteBuffer.put(type.getCode());
        buffer.byteBuffer.putInt(getBodyLength());
        buffer.position(curPosition);
        return buffer.array();
    }

    public static TransferPacket decodeFromByteArray(byte[] data, InetSocketAddress socketAddress) {
        if (data.length < HEAD_LEN) {
            return null;
        }
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
        TransferPacket packet = new TransferPacket(new BodyBuffer(buffer, bodyLength), socketAddress, type);
        packet.setSequence(sequence);
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

    public enum Type {
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

    public final static class BodyBuffer  {
        
        private final ByteBuffer byteBuffer;
        private int bodyLength;
        
        public BodyBuffer() {
            this(TransferServer.MAX_TRANSFER_LEN - HEAD_LEN);
        }
        
        public BodyBuffer(int bodyLength) {
            this(new byte[HEAD_LEN + bodyLength], bodyLength);
        }
        
        public BodyBuffer(byte[] data, int bodyLength) {
            this(ByteBuffer.wrap(data), bodyLength);
        }
        
        private BodyBuffer(ByteBuffer buffer, int bodyLength) {
            this.bodyLength = bodyLength;
            byteBuffer = buffer;
            byteBuffer.position(HEAD_LEN);
        }
        
        public void put(byte value) {
            byteBuffer.put(value);
        }

        public void putInt(int value) {
            byteBuffer.putInt(value);
        }

        public void putLong(long value) {
            byteBuffer.putLong(value);
        }

        public void putChar(char value) {
            byteBuffer.putChar(value);
        }

        public void putString(String value) {
            for (int i = 0;i < value.length();i ++) {
                byteBuffer.putChar(value.charAt(i));
            }
        }
        
        public byte get() {
            return byteBuffer.get();
        }

        public int getInt() {
            return byteBuffer.getInt();
        }

        public long getLong() {
            return byteBuffer.getLong();
        }

        public char getChar() {
            return byteBuffer.getChar();
        }

        public String getString(int length) {
            StringBuilder builder = new StringBuilder(length);
            for (int i = 0;i < length;i ++) {
                builder.append(byteBuffer.getChar());
            }
            return builder.toString();
        }

        public void position(int index) {
            if (index < 0) {
                throw new BufferOverflowException();
            }
            byteBuffer.position(HEAD_LEN + index);
        }

        public int position() {
            return byteBuffer.position() - HEAD_LEN;
        }

        public int limit() {
            return byteBuffer.limit() - HEAD_LEN;
        }

        public byte[] array() {
            return byteBuffer.array();
        }

        protected void setBodyLength(int bodyLength) {
            this.bodyLength = bodyLength;
        }

        public int getBodyLength() {
            return  bodyLength;
        }
    }
}
