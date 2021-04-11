package com.xxkun.relayserver.service.impl;

import com.xxkun.relayserver.dao.UserInfo;
import com.xxkun.relayserver.dao.UserSession;
import com.xxkun.relayserver.dao.mbg.model.User;
import com.xxkun.relayserver.service.RedisService;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserInfoManageServiceImpl implements UserInfoManageService {
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${redis.key.prefix.REDIS_USER_IDENTIFICATION_KEY}")
    private String REDIS_USER_IDENTIFICATION_KEY;
    @Value("${redis.key.expire.EXPIRE_TIME}")
    private Long EXPIRE_TIME;

    @Override
    public boolean isUserLogin(Integer id) {
        return false;
    }

    @Override
    public boolean isExistUserToken(String token) {
        return false;
    }

    @Override
    public UserInfo setUser(User user) {
        String token = UUID.randomUUID().toString();
        redisService.set(token, user.getUserid(), EXPIRE_TIME);
        String identification = UUID.randomUUID().toString();
        redisService.leftPush(REDIS_USER_IDENTIFICATION_KEY + ":" + user.getUserid(), identification);
        return null;
    }

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
}
