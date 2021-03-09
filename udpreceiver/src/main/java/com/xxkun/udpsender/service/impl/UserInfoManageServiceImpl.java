package com.xxkun.udpsender.service.impl;

import com.xxkun.udpsender.dao.UserIdentifier;
import com.xxkun.udpsender.dao.UserInfo;
import com.xxkun.udpsender.dao.UserSession;
import com.xxkun.udpsender.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class UserInfoManageServiceImpl implements UserInfoManageService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserSession getUserSessionFromToken(String token) {
        return null;
    }

    @Override
    public String getUserInfoFromUserId(String userId) {
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
