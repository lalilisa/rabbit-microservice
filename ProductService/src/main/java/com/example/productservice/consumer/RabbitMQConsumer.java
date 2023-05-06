package com.example.productservice.consumer;


import com.example.productservice.controller.ProductControler;
import com.example.productservice.model.MessageData;
import com.example.productservice.model.OderDetail;
import com.example.productservice.model.ProductIds;
import com.example.productservice.repository.ProductRepo;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

@Service
public class RabbitMQConsumer {

    private final static String GET_ALL="GET_ALL";
    private final static String GET_ONE_PRODUCT="GET_ONE_PRODUCT";

    private final static String GET_PRODUCT_BY_IDS="GET_PRODUCT_BY_IDS";

    private final static String UPDATE_AFTER_ORDER="UPDATE_AFTER_ORDER";


    @Autowired
    ProductService productService;

    @Autowired
    private ProductRepo productRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"product-service"})
    @SendTo
    public void consume(MessageData message,
                        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
                        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId
    ) throws JsonProcessingException {
        LOGGER.info(String.format("Received message -> %s", message));
        switch (message.getTarget()){
            case GET_ALL: {
                sendResponse(senderId, correlationId, productService.getAll());
                break;
            }
            case GET_ONE_PRODUCT: {
                 Integer id =(Integer) message.getData();
                 sendResponse(senderId, correlationId,productService.getById(id.longValue()));
                 break;
            }
            case GET_PRODUCT_BY_IDS: {
                ObjectMapper objectMapper=new ObjectMapper();
                String json = objectMapper.writeValueAsString(message.getData());
                System.out.println(message.getData());
                List<Long> productIds=objectMapper.readValue(json, new TypeReference<List<Long>>() {
                });
                sendResponse(senderId, correlationId,productRepo.findAllById(productIds));
                break;
            }

            case UPDATE_AFTER_ORDER:{
                ObjectMapper objectMapper=new ObjectMapper();
                String json = objectMapper.writeValueAsString(message.getData());
                List<OderDetail> details=objectMapper.readValue(json, new TypeReference<List<OderDetail>>() {});
                productService.updateAfterOrder(details);
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
            properties.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return message;
        });
    }
}