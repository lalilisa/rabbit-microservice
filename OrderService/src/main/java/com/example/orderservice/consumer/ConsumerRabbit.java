package com.example.orderservice.consumer;

import com.example.orderservice.model.MessageData;
import com.example.orderservice.model.OrderDto;
import com.example.orderservice.model.OrderQuery;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ConsumerRabbit {

    private static final String GET_ORDER="GET_ORDER";
    private static final String CREATE_ORDER="CREATE_ORDER";

    @Autowired
    private OrderService orderService;
    @RabbitListener(queues = "order-service")
    @SendTo
    public void handeler(MessageData message,
                         @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
                         @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId) throws JsonProcessingException {
        switch (message.getTarget()){
            case GET_ORDER:{
                ObjectMapper objectMapper= new ObjectMapper();
                String json=objectMapper.writeValueAsString(message.getData());
                OrderQuery query=objectMapper.readValue(json,OrderQuery.class);
                sendResponse(senderId,correlationId,orderService.getOrderByUsername(query.getUsername()));
                break;
            }
            case CREATE_ORDER:{
                ObjectMapper objectMapper= new ObjectMapper();
                String json=objectMapper.writeValueAsString(message.getData());
                OrderDto orderDto=objectMapper.readValue(json,OrderDto.class);
                sendResponse(senderId,correlationId,orderService.createOrder(orderDto));
                break;
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

        }
        this.rabbitTemplate.convertAndSend(senderId, json, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setCorrelationId(correlationId);
            return message;
        });
    }
}
