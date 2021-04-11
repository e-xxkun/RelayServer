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
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, String value, long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    @Override
    public boolean hasKey(String key) {
        Boolean k = stringRedisTemplate.hasKey(key);
        return k != null && k;
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public String getValueFromMap(String field, String key) {
        return (String) stringRedisTemplate.opsForHash().get(field, key);
    }

    @Override
    public void setValueToMap(String field, String key, Object value) {
        stringRedisTemplate.opsForHash().put(field, key, value);
    }

    @Override
    public Map<Object, Object> getMap(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    @Override
    public void expire(String key, long auth_code_expire_seconds) {
        stringRedisTemplate.expire(key, auth_code_expire_seconds, TimeUnit.SECONDS);
    }

    @Override
    public long getExpireTime(String key) {
        Long time = stringRedisTemplate.getExpire(key);
        return time == null ? -1L : time;
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public long stringToLong(String str) {
        if (str.length() < 4) {
            return -1;
        }
        long tmp = 0;
        tmp += str.charAt(0);
        tmp += (long)str.charAt(1) << 16;
        tmp += (long)str.charAt(2) << 32;
        tmp += (long)str.charAt(3) << 48;
        return tmp;
    }

    @Override
    public String longToString(long value) {
        char[] charArr = new char[4];
        charArr[0] = (char) (value & 0xFFFF);
        charArr[1] = (char) (value >> 16 & 0xFFFF);
        charArr[2] = (char) (value >> 32 & 0xFFFF);
        charArr[3] = (char) (value >> 48 & 0xFFFF);
        return new String(charArr);
    }
}
