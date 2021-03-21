package com.xxkun.relayserver_udp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UdpreceiverApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdpreceiverApplication.class, args);

//        if (args.length != 1) {
//            System.out.println("Usage: java " + Server.class.getName() + " <port>\n");
//            return;
//        }
//        int port = Integer.parseInt(args[0]);

        int port = 8888;
        new Server().start(port);
    }

}
