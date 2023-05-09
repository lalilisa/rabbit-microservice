package com.example.gatewayservice.controller;

import com.example.gatewayservice.model.MessageData;
import com.example.gatewayservice.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/product")
public class ProductController {

    @Value("${cosmetics.rabbitmq.exchange}")
    private String exchange;


    @Value("${cosmetics.rabbitmq.routingkey-product}")
    private String routingkeyProduct;
    private final RabbitMQProducer rabbitMQProducer;

    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllproduct(){
        Object o=rabbitMQProducer.sendMessage(exchange,routingkeyProduct,new MessageData("smsm","GET_ALL",null));
        return ResponseEntity.ok(o);
    }

    @GetMapping(value = "{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOne(@PathVariable(name = "id") Long id){
        Object o=rabbitMQProducer.sendMessage(exchange,routingkeyProduct,new MessageData("smsm","GET_ONE_PRODUCT",id));
        return ResponseEntity.ok(o);
    }
}
