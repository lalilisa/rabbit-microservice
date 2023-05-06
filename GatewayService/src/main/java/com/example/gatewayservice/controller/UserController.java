package com.example.gatewayservice.controller;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("api/user")
public class UserController {
    @Value("${cosmetics.rabbitmq.exchange}")
    private String exchange;


    @Value("${cosmetics.rabbitmq.routingkey-user}")
    private String routingkeyUser;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping(value = "user-infor",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfo(HttpServletRequest request){
        String username=request.getHeader("token");
        Object o=rabbitTemplate.convertSendAndReceive(exchange,routingkeyUser,username);
        return ResponseEntity.ok(o);
    }
}
