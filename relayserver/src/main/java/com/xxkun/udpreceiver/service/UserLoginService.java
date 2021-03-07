package com.xxkun.udpreceiver.service;

import org.springframework.stereotype.Service;

@Service
public interface UserLoginService {


    String login(String username, String password);

    boolean logout(String token);
}
