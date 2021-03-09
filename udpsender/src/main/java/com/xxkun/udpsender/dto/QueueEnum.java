package com.xxkun.udpsender.dto;

import com.xxkun.udpsender.common.utils.ConstantField;

public enum  QueueEnum implements IQueueEnum{

    HEARTBEAT(ConstantField.QUEUE_HEARTBEAT_EXCHANGE, ConstantField.QUEUE_HEARTBEAT_NAME, ConstantField.QUEUE_HEARTBEAT_ROUTEKEY);

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
