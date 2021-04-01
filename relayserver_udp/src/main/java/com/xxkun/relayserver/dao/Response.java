package com.xxkun.relayserver.dao;

import org.springframework.lang.NonNull;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public final class Response implements Delayed {

    public static final int UDP_MSG_MAX_LEN = 512;

    private static final int MAX_RESEND_TIME = 4;

    private static final int HEAD = 0xFEFDDFEB;

//    HEAD|sequence|cmdId|bodyLength  ->  int|long|int|int
    private static final int HEAD_LEN = 3 * Integer.BYTES + Long.BYTES;

    private Long sequence;

    private Date sendDate;

    private InetSocketAddress socketAddress;

    private ResponseType type;

    private final BodyBuffer bodyBuffer;

    private String id;

    private int resendTime = 0;

    public Response() {
        bodyBuffer = new BodyBuffer();
        bodyBuffer.writeInt(HEAD);
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public long getSendDate() {
        if (sendDate != null)
            return sendDate.getTime();
        else
            return 0;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
        int curIndex = bodyBuffer.byteBuffer.position();
        bodyBuffer.byteBuffer.putLong(Integer.BYTES, sequence);
        bodyBuffer.byteBuffer.position(curIndex);
        refreshId();
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
        refreshId();
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
        int curIndex = bodyBuffer.position();
        bodyBuffer.byteBuffer.putInt(Integer.BYTES + Long.BYTES, type.getCmdId());
        bodyBuffer.position(curIndex);
    }

    public void incResendTime() {
        ++ resendTime;
    }

    public boolean isResend() {
        return resendTime > 0;
    }

    public boolean isMaxResendTime() {
        return resendTime >= MAX_RESEND_TIME;
    }

    public byte[] convertToByteArray() {
        int curIndex = bodyBuffer.position();
        bodyBuffer.byteBuffer.putInt(2 * Integer.BYTES + Long.BYTES, bodyBuffer.getBodyLength());
        bodyBuffer.position(curIndex);
        return bodyBuffer.byteBuffer.array();
    }

    private void refreshId() {
        id = "";
        if (socketAddress != null) {
            id += socketAddress.toString();
        }
        if (sequence != null) {
            id += sequence;
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return getSendDate();
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response udpField = (Response) o;
        return id.equals(udpField.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public BodyBuffer getByteBuffer() {
        return bodyBuffer;
    }

    public class BodyBuffer {

        private ByteBuffer byteBuffer;
        private int bodyLength;

        public BodyBuffer() {
            byteBuffer = ByteBuffer.allocate(UDP_MSG_MAX_LEN);
        }

        public void skip(int length) {
            byteBuffer.position(byteBuffer.position() + length);
        }

        public void reset() {
            byteBuffer.position(HEAD_LEN);
        }

        public void writeInt(int value) {
            byteBuffer.putInt(value);
            bodyLength = Math.max(bodyLength, position());
        }

        public void position(int index) {
            index = Math.max(index, 0);
            byteBuffer.position(HEAD_LEN + index);
        }

        public int position() {
            return byteBuffer.position() - HEAD_LEN;
        }

        public void writeString(String value) {
            for (int i = 0;i < value.length();i ++) {
                byteBuffer.putChar(value.charAt(i));
            }
            bodyLength = Math.max(bodyLength, position());
        }

        public int getBodyLength() {
            return bodyLength;
        }
    }

    public enum ResponseType {

        REPLY(0),
        UNKNOWN(1);

        private final int cmdId;

        ResponseType(int cmdId) {
            this.cmdId = cmdId;
        }

        public int getCmdId() {
            return cmdId;
        }
    }
}
