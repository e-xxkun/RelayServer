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

    private InetSocketAddress socketAddress;

    private Integer rtt;

    private Integer cmdId;

    private Integer clientVersion;

    private Integer bodyLength;

    private String body;

    private String id;

    private int resendTime = 0;

    public UDPField(Long seq, InetSocketAddress socketAddress, Integer rtt, Integer cmdId, Integer clientVersion, Integer bodyLength) {
        this.seq = seq;
        this.socketAddress = socketAddress;
        this.rtt = rtt;
        this.cmdId = cmdId;
        this.clientVersion = clientVersion;
        this.bodyLength = bodyLength;
        this.id = this.socketAddress.toString() + this.seq;
    }

    public Integer getRtt() {
        return rtt;
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
}
