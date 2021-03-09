package com.xxkun.udpsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UdpsenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdpsenderApplication.class, args);

//        if (args.length != 1) {
//            System.out.println("Usage: java " + Server.class.getName() + " <port>\n");
//            return;
//        }
//        int port = Integer.parseInt(args[0]);

        int port = 8887;
        new Server().start(port);
    }

}
