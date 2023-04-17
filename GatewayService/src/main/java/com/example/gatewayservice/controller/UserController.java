package com.example.gatewayservice.controller;

import com.example.gatewayservice.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    @Value("${cosmetics.rabbitmq.exchange}")
    private String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-user}")
    private String routingkey;

    @Value("${cosmetics.rabbitmq.routingkey-product}")
    private String routingkeyP;
    private final RabbitMQProducer rabbitMQProducer;
    @GetMapping()
    public ResponseEntity<?> test(){

        rabbitMQProducer.sendMessage(exchange,routingkey,"TriMV");
        return ResponseEntity.ok("RIMAI");
    }

    @GetMapping("p")
    public ResponseEntity<?> testP(){

        rabbitMQProducer.sendMessage(exchange,"cosmetics.product","TriMVsss");
        return ResponseEntity.ok("sssRIMAI");
    }

    @GetMapping("c")
    public ResponseEntity<?> testC(){

        rabbitMQProducer.sendMessage(exchange,"cosmetics.cart","Meoemoe");
        return ResponseEntity.ok("sssRIMAI");
    }
}
