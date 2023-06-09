package com.example.productservice.config;

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

    @Value("${cosmetics.rabbitmq.queue-product}")
    String queueName;

    @Value("${cosmetics.rabbitmq.exchange}")
    String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-product}")
    private String routingkey;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }
    @Bean
    Queue queueProduct() {
        return new Queue(queueName, false);
    }
    @Bean
    Binding bindingProduct(DirectExchange exchange) {
        return BindingBuilder.bind(queueProduct()).to(exchange).with(routingkey);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
