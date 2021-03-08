package com.xxkun.udpreceiver.config;

import com.xxkun.udpreceiver.dto.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    DirectExchange heartbeatDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(QueueEnum.HEARTBEAT.getExchange())
                .durable(true)
                .build();
    }

    @Bean
    public Queue heartbeatQueue() {
        return new Queue(QueueEnum.HEARTBEAT.getName());
    }

    @Bean
    Binding heartbeatBinding(DirectExchange heartbeatDirect, Queue heartbeatQueue){
        return BindingBuilder
                .bind(heartbeatQueue)
                .to(heartbeatDirect)
                .with(QueueEnum.HEARTBEAT.getRouteKey());
    }
}
