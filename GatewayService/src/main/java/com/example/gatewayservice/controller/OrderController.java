package com.example.gatewayservice.controller;

import com.example.gatewayservice.dto.CartRabbitRq;
import com.example.gatewayservice.dto.OderDetail;
import com.example.gatewayservice.dto.OrderDto;
import com.example.gatewayservice.dto.OrderQuery;
import com.example.gatewayservice.model.MessageData;
import com.example.gatewayservice.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {
    @Value("${cosmetics.rabbitmq.exchange}")
    private String exchange;


    @Value("${cosmetics.rabbitmq.routingkey-order}")
    private String routingkeyOrder;
    private final RabbitMQProducer rabbitMQProducer;


    @GetMapping(value = "my-order",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMyOrder(HttpServletRequest request){
        String username=request.getHeader("token");
        OrderQuery orderQuery= OrderQuery.builder().username(username).build();
        Object o=rabbitMQProducer.sendMessage(exchange,routingkeyOrder,new MessageData("smsm","GET_ORDER",orderQuery));
        return ResponseEntity.ok(o);
    }

    @PostMapping(value = "create-order",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMyOrder(HttpServletRequest request,
                                           @RequestBody List<OderDetail> oderDetail,
                                           @RequestParam("phone") String phone,
                                           @RequestParam("address") String address,
                                           @RequestParam("name") String name
                                           ){
        String username=request.getHeader("token");
        System.out.println(oderDetail);
        OrderDto orderDto=OrderDto.builder()
                    .username(username)
                    .phonenumber(phone)
                    .name(name)
                    .address(address)
                    .details(oderDetail).build();
        Object o=rabbitMQProducer.sendMessage(exchange,routingkeyOrder,new MessageData("smsm","CREATE_ORDER",orderDto));
        return ResponseEntity.ok(o);
    }

}
