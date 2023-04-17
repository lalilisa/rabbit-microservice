package com.example.productservice.consumer;


import com.example.productservice.controller.ProductControler;
import com.example.productservice.model.MessageData;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private final static String GET_ALL="GET_ALL";


    @Autowired
    ProductControler productControler;

    @Value(value = "${cosmetics.rabbitmq.queue-product}")
    private String queue;

    @Value(value = "${cosmetics.rabbitmq.routingkey-product}")
    private String routingKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"product-service"})
    @SendTo
    public void consume(MessageData message,
                        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
                        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId
    ){
        LOGGER.info(String.format("Received message -> %s", message));
        switch (message.getTarget()){
            case GET_ALL:
                    sendResponse(senderId,correlationId,productControler.getAll());

        }

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
            properties.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return message;
        });
    }
}