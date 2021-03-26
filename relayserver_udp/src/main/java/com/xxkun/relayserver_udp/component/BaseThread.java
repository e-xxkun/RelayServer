package com.xxkun.relayserver_udp.component;

public class BaseThread extends Thread {

    protected boolean stop = false;

    public void close() {
        stop = true;
    }
}
