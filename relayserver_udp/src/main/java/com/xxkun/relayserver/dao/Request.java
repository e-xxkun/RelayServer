package com.xxkun.relayserver.dao;

import com.xxkun.relayserver.component.exception.RequestResolutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public final class Request {

    @Value("udpserver.MSG_BUFF_LEN.request")
    public static final int UDP_MSG_MAX_LEN = 512;

    private static final int MAX_RESEND_TIME = 4;

    private static final int HEAD = 0xFEFDDFEB;

    private static final int HEAD_LEN = 4 * Integer.BYTES + Long.BYTES;

    private Long sequence;

    private Date receiveDate;

    private final InetSocketAddress socketAddress;

    private final RequestType type;

    private final Integer clientVersion;

    private final BodyBuffer bodyBuffer;

    private Request(ByteBuffer buffer, Long seq, Integer clientVersion, Integer cmdId, Integer bodyLength, @NonNull InetSocketAddress socketAddress) {
        this.bodyBuffer = new BodyBuffer(buffer, bodyLength);
        this.sequence = seq;
        this.socketAddress = socketAddress;
        this.type = RequestType.valueOf(cmdId);
        this.clientVersion = clientVersion;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public RequestType getType() {
        return type;
    }

    public Integer getClientVersion() {
        return clientVersion;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public static Request decodeFromByteArray(@NonNull byte[] bytes, InetSocketAddress socketAddress) throws RequestResolutionException {
        if (bytes.length < HEAD_LEN)
            throw new RequestResolutionException();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        // HEAD|sequence|clientVersion|cmdId|bodyLength  ->  int|long|int|int|int

        if (buffer.getInt() != HEAD)
            throw new RequestResolutionException();
        long seq = buffer.getLong();

        int clientVersion = buffer.getInt();

        int cmdId = buffer.getInt();
        if (cmdId < 0 || cmdId >= RequestType.values().length - 1)
            throw new RequestResolutionException();

        int bodyLength = buffer.getInt();

        return new Request(buffer, seq, clientVersion, cmdId, bodyLength, socketAddress);
    }

    public BodyBuffer getByteBuffer() {
        return bodyBuffer;
    }

    public class BodyBuffer {

        private int bodyLength;

        private ByteBuffer byteBuffer;

        public BodyBuffer(ByteBuffer byteBuffer, Integer bodyLength) {
            this.byteBuffer = byteBuffer;
            this.bodyLength = bodyLength;
        }

        public int getInt() {
            if (Integer.BYTES > remainLength()) {
                return -1;
            }
            return bodyBuffer.getInt();
        }

        public boolean skip(int length) {
            if (length > remainLength()) {
                return false;
            }
            byteBuffer.position(byteBuffer.position() + length);
            return true;
        }

        public int remainLength() {
            return HEAD_LEN + bodyLength - byteBuffer.position();
        }

        public void reset() {
            byteBuffer.position(HEAD_LEN);
        }

        public String getString(int length) {
            if (Character.BYTES * length > remainLength()) {
                return null;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0;i < length;i ++) {
                builder.append(byteBuffer.getChar());
            }
            return builder.toString();
        }
    }

    public enum RequestType {
        ACK(0) {
            @Override
            public boolean isACK() {
                return true;
            }
        },
        PUT(1) {
            @Override
            public boolean isPUT() {
                return true;
            }
        },
        GET(2) {
            @Override
            public boolean isGET() {
                return true;
            }
        },
        UNKNOWN(3);

        private final int cmdId;

        RequestType(int cmdId) {
            this.cmdId = cmdId;
        }

        public static RequestType valueOf(int cmdId) {
            if (cmdId > -1 && cmdId < RequestType.values().length) {
                return RequestType.values()[cmdId];
            }
            return UNKNOWN;
        }

        public int getCmdId() {
            return cmdId;
        }

        public boolean isACK() {
            return false;
        }

        public boolean isPUT() {
            return false;
        }

        public boolean isGET() {
            return false;
        }
    }
}
