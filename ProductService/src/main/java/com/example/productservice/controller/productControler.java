package com.example.productservice.controller;

import com.example.productservice.consumer.RabbitMQConsumer;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class productControler {

    @Autowired
    ProductService productService;


    @Autowired
    RabbitMQConsumer rabbitMQConsumer;
    public void getAll(){
        List<Product> list = productService.findAll();
        rabbitMQConsumer.sendResponse("", "", list);
    }

    public void getById(Long id){
        Product product = productService.getProductById(id);
        rabbitMQConsumer.sendResponse("", "", product);
    }
}
