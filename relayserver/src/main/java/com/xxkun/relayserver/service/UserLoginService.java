package com.xxkun.relayserver.service;

import com.xxkun.relayserver.component.exception.ReloginException;
import com.xxkun.relayserver.component.exception.UserNotExistException;
import org.springframework.stereotype.Service;

@Service
public interface UserLoginService {

    String login(String username, String password) throws UserNotExistException, ReloginException;

    boolean logout(String token);
}
