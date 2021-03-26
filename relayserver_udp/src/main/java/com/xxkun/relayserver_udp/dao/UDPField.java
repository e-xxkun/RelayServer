package com.xxkun.relayserver_udp.dao;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class UDPField implements Delayed {

    private static final int MAX_RESEND_TIME = 4;

    public static final String HEAD = "US";

    private Long seq;

    private Date sendDate;

    private Date receiveDate;

    private final InetSocketAddress socketAddress;

    private final Integer rtt;

    private final UDPFieldType type;

    private final Integer clientVersion;

    private final Integer bodyLength;

    private String body;

    private final String id;

    private int resendTime = 0;

    public UDPField(Long seq, InetSocketAddress socketAddress, Integer rtt, Integer cmdId, Integer clientVersion, Integer bodyLength) {
        this.seq = seq;
        this.socketAddress = socketAddress;
        this.rtt = rtt;
        this.type = UDPFieldType.valueOf(cmdId);
        this.clientVersion = clientVersion;
        this.bodyLength = bodyLength;
        this.id = this.socketAddress.toString() + this.seq;
    }

    public Integer getRtt() {
        return rtt;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public UDPFieldType getType() {
        return type;
    }

    public Integer getClientVersion() {
        return clientVersion;
    }

    public Integer getBodyLength() {
        return bodyLength;
    }

    public long getSendDate() {
        return sendDate.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UDPField udpField = (UDPField) o;
        return id.equals(udpField.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return getSendDate();
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public InetSocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void incResendTime() {
        ++ resendTime;
    }

    public boolean isMaxResendTime() {
        return resendTime >= MAX_RESEND_TIME;
    }

    public boolean isResend() {
        return resendTime > 0;
    }

    public boolean isACK() {
        return type.isACK();
    }

    public enum UDPFieldType {
        ACK(0){
            @Override
            public boolean isACK() {
                return true;
            }
        },
        UNKNOWN(1);

        private final int cmdId;

        UDPFieldType(int cmdId) {
            this.cmdId = cmdId;
        }

        public static UDPFieldType valueOf(int cmdId) {
            if (cmdId > -1 && cmdId < UDPFieldType.values().length) {
                return UDPFieldType.values()[cmdId];
            }
            return UNKNOWN;
        }

        public int getCmdId() {
            return cmdId;
        }

        public boolean isACK() {
            return false;
        }
    }
}
