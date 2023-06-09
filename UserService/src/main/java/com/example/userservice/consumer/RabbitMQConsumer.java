package com.example.userservice.consumer;


import com.example.userservice.model.MessageData;

import com.example.userservice.model.OrderDto;
import com.example.userservice.model.UsernameResponse;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Value(value = "${cosmetics.rabbitmq.queue-user}")
    private String queue;

    @Value(value = "${cosmetics.rabbitmq.routingkey-user}")
    private  static final String CREATE_USER="CREATE_USER";
    private  static final String GET_USER="GET_USER";
    private  static final String UPDATE_AFTER_ORDER="UPDATE_AFTER_ORDER";
    private String routingKey;
    @Autowired
    private UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"user-service"})
    @SendTo
    public void consume(MessageData message,
                        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
                        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId
    ) throws JsonProcessingException {
        LOGGER.info(String.format("Received message -> %s", message));
        switch (message.getTarget()){
            case CREATE_USER:{
                String username= userService.createUser();
                sendResponse(senderId,correlationId, UsernameResponse.builder().username(username).build());
                break;
            }
            case GET_USER:{
                System.out.println(message.getData());
                String username= (String) message.getData();
                sendResponse(senderId,correlationId, userService.getUserByUsername(username));
                break;
            }
            case UPDATE_AFTER_ORDER:{
                ObjectMapper objectMapper= new ObjectMapper();
                String json=objectMapper.writeValueAsString(message.getData());
                OrderDto orderDto=objectMapper.readValue(json,OrderDto.class);
                userService.updateUser(orderDto.getUsername(),orderDto.getName(),orderDto.getAddress(),orderDto.getPhonenumber());
            }
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
            return message;
        });
    }
}
