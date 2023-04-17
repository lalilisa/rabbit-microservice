package com.example.cartservice.controller;

import com.example.cartservice.consumer.RabbitMQConsumer;
import com.example.cartservice.entity.Cart;
import com.example.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    RabbitMQConsumer rabbitMQConsumer;

    public void getCart(Long id){
        List<Cart> list = cartService.getCartByUserId(id);
        rabbitMQConsumer.sendResponse("", "", list);
    }

    public void buyProduct(List<Long> ids){
        List<Cart> list = cartService.findAll();
        for(Cart cart: list){
            if(ids.contains(cart.getId())){
                cart.setStatus(0);
                cartService.save(cart);
            }
        }
        rabbitMQConsumer.sendResponse("", "", "success");
    }
}
