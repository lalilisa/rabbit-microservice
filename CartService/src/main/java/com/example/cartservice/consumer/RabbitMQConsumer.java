package com.example.cartservice.consumer;



import com.example.cartservice.model.CartCommand;
import com.example.cartservice.model.MessageData;
import com.example.cartservice.model.OrderDto;
import com.example.cartservice.service.CartService;
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

    private static final String ADD_TO_CART="ADD_TO_CART";
    private static final String GET_CART="GET_CART";
    private static final String UPDATE_AFTER_ORDER="UPDATE_AFTER_ORDER";
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    @Autowired
    private CartService cartService;
    @RabbitListener(queues = {"cart-service"})
    @SendTo
    public void consume(MessageData message,
                        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
                        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId
    ) throws JsonProcessingException {
        LOGGER.info(String.format("Received message -> %s", message));
        switch (message.getTarget()){
            case ADD_TO_CART:{
                ObjectMapper objectMapper= new ObjectMapper();
                String json=objectMapper.writeValueAsString(message.getData());
                CartCommand cartCommand=objectMapper.readValue(json,CartCommand.class);
                System.out.println(cartCommand);
                sendResponse(senderId,correlationId,cartService.addtoCart(cartCommand));
                break;
            }
            case GET_CART:{
                sendResponse(senderId,correlationId,cartService.getCart((String) message.getData()));
                break;
            }
            case UPDATE_AFTER_ORDER:{
                ObjectMapper objectMapper= new ObjectMapper();
                String json=objectMapper.writeValueAsString(message.getData());
                OrderDto orderDto=objectMapper.readValue(json,OrderDto.class);
                cartService.updateAfterOrder(orderDto);
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
            LOGGER.info(exception.getMessage());
        }
        this.rabbitTemplate.convertAndSend(senderId, json, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setCorrelationId(correlationId);
            return message;
        });
    }
}