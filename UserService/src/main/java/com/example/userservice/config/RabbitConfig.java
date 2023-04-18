package com.example.userservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${cosmetics.rabbitmq.queue-user}")
    String queueName;

    @Value("${cosmetics.rabbitmq.exchange}")
    String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-user}")
    private String routingkey;

    @Bean
    Queue queueUser() {
        return new Queue(queueName, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding bindingUser(DirectExchange exchange) {
        return BindingBuilder.bind(queueUser()).to(exchange).with(routingkey);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
