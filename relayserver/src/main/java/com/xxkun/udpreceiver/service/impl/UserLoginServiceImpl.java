package com.xxkun.udpreceiver.service.impl;

import cn.hutool.json.JSONObject;
import com.xxkun.udpreceiver.dao.mbg.mapper.UserMapper;
import com.xxkun.udpreceiver.dao.mbg.model.User;
import com.xxkun.udpreceiver.dao.mbg.model.UserExample;
import com.xxkun.udpreceiver.service.RedisService;
import com.xxkun.udpreceiver.service.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserLoginServiceImpl implements UserLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${redis.key.prefix.REDIS_USER_IDENTIFICATION_KEY}")
    private String REDIS_USER_IDENTIFICATION_KEY;
    @Value("${redis.key.expire.EXPIRE_TIME}")
    private Long EXPIRE_TIME;

    @Override
    public String login(String userId, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUseridEqualTo(userId);
        userExample.createCriteria().andPasswordEqualTo(password);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList != null && userList.size() > 0) {
            User user = userList.get(0);
            if (redisService.hasKey(REDIS_USER_IDENTIFICATION_KEY + ":" + user.getUserid())) {
                return null;
            }
            String token = UUID.randomUUID().toString();
            redisService.set(REDIS_USER_SESSION_KEY + ":" + token, user.getUserid(), EXPIRE_TIME);
            String identification = UUID.randomUUID().toString();
            redisService.leftPush(REDIS_USER_IDENTIFICATION_KEY + ":" + user.getUserid(), identification);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOnce("token", token);
            jsonObject.putOnce("identification", identification);
            return jsonObject.toString();
        }
        return null;
    }

    @Override
    public boolean logout(String token) {
        String userId = redisService.get(token);
        if (userId == null) {
            return false;
        }
        redisService.remove(token);
        redisService.remove(REDIS_USER_IDENTIFICATION_KEY + ":" + userId);
        return true;
    }
}
