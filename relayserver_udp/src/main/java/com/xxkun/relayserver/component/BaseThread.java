package com.xxkun.relayserver.component;

public class BaseThread extends Thread {

    protected boolean stop = false;

    public void close() {
        stop = true;
    }
}
