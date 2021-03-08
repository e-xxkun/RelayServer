package com.xxkun.udpreceiver.dto;

public enum  QueueEnum implements IQueueEnum{

    HEARTBEAT("user.heartbeat.direct", "user.heartbeat", "user.heartbeat");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }

    @Override
    public String getExchange() {
        return exchange;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRouteKey() {
        return routeKey;
    }
}
