package com.xxkun.relayserver.common;

public class BaseThread extends Thread {

    protected boolean stop = false;

    public void close() {
        stop = true;
    }
}