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
    public void setHashValue(String key1, String key2, Object value) {
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
