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
public class QueueProductService {
    @Value("${cosmetics.rabbitmq.queue-product}")
    String queueName;

    @Value("${cosmetics.rabbitmq.exchange}")
    String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-product}")
    private String routingkey;

    @Bean
    Queue queueProduct() {
        return new Queue(queueName, false);
    }
    @Bean
    Binding bindingProduct(DirectExchange exchange) {
        return BindingBuilder.bind(queueProduct()).to(exchange).with(routingkey);
    }
}
