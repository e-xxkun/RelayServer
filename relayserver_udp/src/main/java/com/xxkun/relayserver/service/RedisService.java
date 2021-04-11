package com.xxkun.relayserver.service;

import java.util.Map;

public interface RedisService {

    void set(String key, String value);

    void set(String key, String value, long time);

    boolean hasKey(String key);

    String get(String key);

    void setValueToMap(String field, String key, Object value);

    Map<Object, Object> getMap(String key);

    String getValueFromMap(String field, String key);


    /**
     * @describe: 设置超时时间
     * @param key
     * @param auth_code_expire_seconds
     * @date 2020/12/20 18:08
     */
    void expire(String key, long auth_code_expire_seconds);

    long getExpireTime(String key);

    void remove(String key);

    Long increment(String key, long delta);

    long stringToLong(String str);

    String longToString(long value);
}
