package com.xxkun.relayserver_udp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ListenServerConfiguration {

    @Bean
    public DatagramSocket serverSocket() {
        DatagramSocket serverSocket = null;
        try {
             serverSocket = new DatagramSocket(8876);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }
}
