package com.xxkun.relayserver.service.impl;

import com.xxkun.relayserver.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public void leftPush(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public void trim(String key, long start, long end) {
        stringRedisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void expire(String key, long auth_code_expire_seconds) {
        stringRedisTemplate.expire(key, auth_code_expire_seconds, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }
}
