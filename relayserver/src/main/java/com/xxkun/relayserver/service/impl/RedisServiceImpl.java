package com.xxkun.relayserver.service.impl;

import com.xxkun.relayserver.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setHashValue(String key1, String key2, String value) {
        stringRedisTemplate.opsForHash().put(key1, key2, value);
    }

    @Override
    public void setHashMap(String key, Map<?, ?> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void set(String key, String value, long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
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
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void expire(String key, long time) {
        stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
    }
}
