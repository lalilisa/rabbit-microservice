package com.example.gatewayservice.producer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class RabbitMQProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);


    @Qualifier("rabbitBean")
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Object sendMessage(String exchange,String routingKey,Object message){
        LOGGER.info(String.format("Message sent -> %s", message));
        return  rabbitTemplate.convertSendAndReceive(exchange, routingKey, message);
    }
}
