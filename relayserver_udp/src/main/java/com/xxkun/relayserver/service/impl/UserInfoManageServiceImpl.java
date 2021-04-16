package com.xxkun.relayserver.service.impl;

import com.xxkun.relayserver.common.TokenProducer;
import com.xxkun.relayserver.pojo.user.UserIdentifier;
import com.xxkun.relayserver.pojo.user.UserInfo;
import com.xxkun.relayserver.pojo.user.UserSession;
import com.xxkun.relayserver.pojo.NatType;
import com.xxkun.relayserver.pojo.user.UserStatus;
import com.xxkun.relayserver.service.RedisService;
import com.xxkun.relayserver.service.UserInfoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserInfoManageServiceImpl implements UserInfoManageService {

    @Autowired
    private RedisService redisService;
    @Value("${redis.key.expire.EXPIRE_TIME}")
    private Long EXPIRE_TIME;
    @Value("${redis.key.expire.EXPIRE_TIME_RATIO}")
    private Float EXPIRE_TIME_RATIO;

    @Override
    public UserSession getUserSessionFromToken(String token) {
        String idStr = redisService.get(token);
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
        userInfo.setStatus(UserStatus.fromCode((Integer) userMap.get("STATE")));
        userInfo.setNatType(NatType.fromCode((Integer) userMap.get("TYPE")));
        userInfo.setIdentifier(new UserIdentifier((String) userMap.get("IDENTIFIER")));
        return userInfo;
    }

    @Override
    public UserInfo getUserInfoFromUserSession(@NonNull UserSession userSession) {
        UserInfo info = getUserInfoFromUserId(userSession.getUserId());
        info.setSession(userSession);
        return info;
    }

    @Override
    public UserInfo refreshUserSession(@NonNull UserSession userSession) {
        String idStr = redisService.longToString(userSession.getUserId());
        UserInfo info = new UserInfo(userSession.getUserId());
        String identifierStr = redisService.getValueFromMap(idStr, "IDENTIFIER");
        UserIdentifier userIdentifier = new UserIdentifier(identifierStr);
        identifierStr = userIdentifier.update();
        redisService.setValueToMap(idStr, "IDENTIFIER", identifierStr);
        info.setIdentifier(userIdentifier);
        String token = TokenProducer.get(userSession.getUserId());
        redisService.expire(idStr, EXPIRE_TIME);
        redisService.set(token, idStr, EXPIRE_TIME);
        redisService.remove(userSession.getToken());
        userSession.setToken(token);
        info.setSession(userSession);
        return  info;
    }

    @Override
    public boolean isUserSessionExpire(@NonNull UserSession userSession) {
        long expireTime = redisService.getExpireTime(userSession.getToken());
        return expireTime < EXPIRE_TIME * EXPIRE_TIME_RATIO;
    }
}
