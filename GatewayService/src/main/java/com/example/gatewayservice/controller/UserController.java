package com.example.gatewayservice.controller;


import com.example.gatewayservice.model.MessageData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController()
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/user")
public class UserController {
    @Value("${cosmetics.rabbitmq.exchange}")
    private String exchange;


    @Value("${cosmetics.rabbitmq.routingkey-user}")
    private String routingkeyUser;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserInfo(HttpServletRequest request){
        String username=request.getHeader("token");
        Object o=rabbitTemplate.convertSendAndReceive(exchange,routingkeyUser,new MessageData("ss","GET_USER",username));
        return ResponseEntity.ok(o);
    }
}
