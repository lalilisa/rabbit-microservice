package com.example.orderservice.service;

import com.example.orderservice.domain.OrderDetail;
import com.example.orderservice.domain.Orders;
import com.example.orderservice.model.*;
import com.example.orderservice.repo.OrderDetailRepository;
import com.example.orderservice.repo.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class OrderService {

    @Value("${cosmetics.rabbitmq.exchange}")
    String exchange;

    @Value("${cosmetics.rabbitmq.routingkey-user}")
    private String routingkeyUser;

    @Value("${cosmetics.rabbitmq.routingkey-product}")
    private String routingkeyProduct;

    @Value("${cosmetics.rabbitmq.routingkey-cart}")
    private String routingkeyCart;

    @Autowired
    private  OrderRepository orderRepository;

    @Autowired
    private  RabbitTemplate rabbitTemplate;

    public List<OrderView> getOrderByUsername(String username) throws JsonProcessingException {
        Object o=rabbitTemplate.convertSendAndReceive(exchange,routingkeyUser,new MessageData("","GET_USER",username));
        ObjectMapper objectMapper=new ObjectMapper();
        User user;
        user=objectMapper.readValue(o.toString(),User.class);
        if(user==null)
            return new ArrayList<>();
        List<Orders>orders= orderRepository.findOrdersByUserId(user.getId());
        return orders.stream().map(orders1 -> {
            List<OrderItemVIew> orderDetailView=convertToView(orders1);
            return OrderView.builder()
                    .name(user.getName())
                    .address(user.getAddress())
                    .orderItemVIews(orderDetailView)
                    .username(user.getUsername())
                    .phonenumber(user.getPhone())
                    .build();
        }).collect(Collectors.toList());

    }

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    private List<OrderItemVIew> convertToView(Orders domain){
        List<OrderDetail> orderDetail=orderDetailRepository.findByOrderId(domain.getId());
        return  orderDetail.stream().map(this::orderItemVIew).collect(Collectors.toList());

    }

    private OrderItemVIew orderItemVIew(OrderDetail domain){
        return OrderItemVIew.builder()
                .img(domain.getImg())
                .quantity(domain.getQuantity())
                .productId(domain.getProductId())
                .price(domain.getPrice())
                .productName(domain.getProductName())
                .build();
    }

    public OrderView createOrder(OrderDto orderDto) throws JsonProcessingException {
        System.out.println(orderDto);
        Object o=rabbitTemplate.convertSendAndReceive(exchange,routingkeyUser,new MessageData("","GET_USER",orderDto.getUsername()));
        ObjectMapper objectMapper=new ObjectMapper();
        User user;
        if(o==null)
            return new OrderView();
        user=objectMapper.readValue(o.toString(),User.class);
        if(user==null)
            return new OrderView();
        List<Long> idsProduct=orderDto.getDetails().stream().map(OderDetail::getProductId).collect(Collectors.toList());
        List<OderDetail> details=orderDto.getDetails();
        ProductIds productIds=ProductIds.builder().ids(idsProduct).build();
        System.out.println(productIds);
        Object productO=rabbitTemplate
                .convertSendAndReceive(exchange,routingkeyProduct,
                        new MessageData("","GET_PRODUCT_BY_IDS",idsProduct));
        if(productO==null){
            return  new OrderView();
        }
        List<Product> products =objectMapper.readValue(productO.toString(),new TypeReference<List<Product>>() {});
        if(products==null||products.isEmpty()){
            return new OrderView();
        }
        for (Product product:products){
            OderDetail orderDetail=details.stream().filter(oderDetail -> oderDetail.getProductId().equals(product.getId())).findFirst().orElse(null);
            if(orderDetail.getQuantity()>product.getQuantity()) {
                throw new RuntimeException("ssss");
            }
        }
        Orders order =orderRepository.save(Orders.builder()
                .userId(user.getId())
                .build());
        List<OrderDetail> orderDetails=products.stream().map(product -> {
            OderDetail orderDetail=details.stream().filter(oderDetail -> oderDetail.getProductId().equals(product.getId())).findFirst().orElse(null);
            return OrderDetail.builder()
                    .orderId(order.getId())
                    .img(product.getImg())
                    .price(product.getPrice())
                    .productName(product.getName())
                    .productId(product.getId())
                    .quantity(orderDetail.getQuantity())
                    .build();
        }).collect(Collectors.toList());
         orderDetailRepository.saveAll(orderDetails);
        rabbitTemplate.convertAndSend(exchange,routingkeyProduct,new MessageData("ss","UPDATE_AFTER_ORDER",orderDto.getDetails()));
        rabbitTemplate.convertAndSend(exchange,routingkeyUser,new MessageData("ss","UPDATE_AFTER_ORDER",orderDto));
        rabbitTemplate.convertAndSend(exchange,routingkeyCart,new MessageData("ss","UPDATE_AFTER_ORDER",orderDto));
        return OrderView.builder()
                .name(orderDto.getName())
                .address(orderDto.getAddress())
                .orderItemVIews(convertToView(order))
                .username(user.getUsername())
                .phonenumber(orderDto.getPhonenumber())
                .build();
    }
}
