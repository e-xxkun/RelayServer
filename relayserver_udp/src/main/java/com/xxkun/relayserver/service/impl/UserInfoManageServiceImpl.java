package com.xxkun.relayserver.service.impl;

import com.xxkun.relayserver.dao.UserIdentifier;
import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.service.RedisService;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserInfoManageServiceImpl implements UserInfoManageService {

    @Autowired
    private RedisService redisService;

    @Override
    public UserSession getUserSessionFromToken(String token) {
        String idStr = redisService.getValueFromMap("SESSION", token);
        if (idStr == null) {
            return null;
        }
        long userId = redisService.stringToLong(idStr);
        if (userId < 0) {
            return null;
        }
        UserSession session = new UserSession(userId);
        session.setToken(token);
        return session;
    }

    @Override
    public UserInfo getUserInfoFromUserId(long userId) {
        String idStr = redisService.longToString(userId);
        Map<Object, Object> userMap = redisService.getMap(idStr);
        UserInfo userInfo = new UserInfo(userId);
        userInfo.setName((String) userMap.get("NAME"));
        userInfo.setUrl((String) userMap.get("URL"));
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoFromUserSession(UserSession userSession) {
        return null;
    }

    @Override
    public UserIdentifier updateUserInfo(UserSession userInfo) {
        return null;
    }

    @Override
    public void setUserSession(UserSession userSession) {

    }

    @Override
    public UserInfo refreshUserSession(UserSession userSession) {
        return null;
    }

    @Override
    public boolean isUserSessionExpire() {
        return false;
    }
}
