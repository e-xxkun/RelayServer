package com.xxkun.relayserver;

import com.xxkun.relayserver.receive.RequestListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UdpserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdpserverApplication.class, args);

    }

}
