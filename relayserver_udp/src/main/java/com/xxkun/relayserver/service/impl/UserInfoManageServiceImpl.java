package com.xxkun.relayserver.service.impl;

import com.xxkun.relayserver.dao.UserIdentifier;
import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserInfoManageServiceImpl implements UserInfoManageService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserSession getUserSessionFromToken(String token) {
        return null;
    }

    @Override
    public UserInfo getUserInfoFromUserId(long userId) {
        return null;
    }

    @Override
    public UserInfo getUserInfoFromUserSession(UserInfo userInfo) {
        return null;
    }

    @Override
    public void updateUserSession(UserSession userSession) {

    }

    @Override
    public UserIdentifier updateUserInfo(UserSession userInfo) {

        return null;
    }

    @Override
    public void setUserSession(UserSession userSession) {

    }
}
