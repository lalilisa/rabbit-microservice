package com.example.gatewayservice.controller;

import com.example.gatewayservice.dto.CartDto;
import com.example.gatewayservice.dto.CartRabbitRq;
import com.example.gatewayservice.model.MessageData;
import com.example.gatewayservice.producer.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/cart")
@CrossOrigin
public class CartController {

    @Value("${cosmetics.rabbitmq.exchange}")
    private String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-cart}")
    private String routingkeyCart;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @PostMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOne(@RequestBody CartDto cartDto, HttpServletRequest request){
        String username=request.getHeader("token");
        CartRabbitRq cartRabbitRq=CartRabbitRq.builder()
                .productId(cartDto.getProductId())
                .quantity(cartDto.getQuantity())
                .username(username)
                .build();
        Object o=rabbitMQProducer.sendMessage(exchange,routingkeyCart,new MessageData("smsm","ADD_TO_CART",cartRabbitRq));
        return ResponseEntity.ok(o);
    }

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCart(HttpServletRequest request){
        String username=request.getHeader("token");
        Object o=rabbitMQProducer.sendMessage(exchange,routingkeyCart,new MessageData("smsm","GET_CART",username));
        return ResponseEntity.ok(o);
    }
}
