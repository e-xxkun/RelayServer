package com.xxkun.udpsender.dao;

import java.net.InetSocketAddress;
import java.util.Date;

public class UDPField {

    public static final String HEAD = "US";

    private String seq;

    private Date sendDate;

    private Date receiveDate;

    private InetSocketAddress socketAddress;

    private Integer rtt;

    private Integer cmdId;

    private Integer clientVersion;

    private Integer bodyLength;

    private String body;

    private String id;

    public UDPField(String seq, InetSocketAddress socketAddress, Integer rtt, Integer cmdId, Integer clientVersion, Integer bodyLength) {
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
}
