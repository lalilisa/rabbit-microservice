package com.example.cartservice.consumer;


import com.example.cartservice.model.MessageData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {


    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"cart-service"})
    @SendTo
    public void consume(MessageData message,
                        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
                        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId
    ){
        LOGGER.info(String.format("Received message -> %s", message));
    }
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendResponse(String senderId, String correlationId, Object data)  {
        String json=null;
        try {
            json = new ObjectMapper().writeValueAsString(data);
        }
        catch (Exception exception){
            LOGGER.info(exception.getMessage());
        }
        this.rabbitTemplate.convertAndSend(senderId, json, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setCorrelationId(correlationId);
            return message;
        });
    }
}