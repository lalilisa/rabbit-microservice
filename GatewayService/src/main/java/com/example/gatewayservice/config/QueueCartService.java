package com.example.gatewayservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class QueueCartService {
    @Value("${cosmetics.rabbitmq.queue-cart}")
    String queueName;

    @Value("${cosmetics.rabbitmq.exchange}")
    String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-cart}")
    private String routingkey;

    @Bean
    Queue queueCart() {
        return new Queue(queueName, false);
    }

    @Bean
    Binding bindingCart(DirectExchange exchange) {
        return BindingBuilder.bind(queueCart()).to(exchange).with(routingkey);
    }
}
