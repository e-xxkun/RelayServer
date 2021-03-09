package com.xxkun.udpsender.service;

public interface RedisService {

    void set(String key, String value);

    void set(String key, String value, long time);

    void leftPush(String key, String value);

    void trim(String key, long start, long end);

    boolean hasKey(String key);

    String get(String key);

    /**
     * @describe: 设置超时时间
     * @param key
     * @param auth_code_expire_seconds
     * @date 2020/12/20 18:08
     */
    void expire(String key, long auth_code_expire_seconds);

    void remove(String key);

    Long increment(String key, long delta);
}
