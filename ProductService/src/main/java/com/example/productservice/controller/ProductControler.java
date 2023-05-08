package com.example.productservice.controller;

import com.example.productservice.consumer.RabbitMQConsumer;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.model.ImageView;
import com.example.productservice.model.ProductView;
import com.example.productservice.service.ImageService;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductControler {

    @Autowired
    ProductService productService;

    @Autowired
    ImageService imageService;


    public List<ProductView> getAll(){
        List<ProductView> list = new ArrayList<>();
        List<Product> listProduct = productService.findAll();
        for(Product pr: listProduct){
            ProductView product = new ProductView();
            product.setId(pr.getId());
            product.setName(pr.getName());
            product.setDescription(pr.getDescription());
            product.setPrice(pr.getPrice());
            product.setCreateDate(pr.getCreateDate());
            product.setQuantity(pr.getQuantity());
            List<ImageView> imageViewList = new ArrayList<>();
            imageViewList.add(ImageView.builder().image(pr.getImg()).build());

            product.setImages(imageViewList);
            list.add(product);
        }
        return list;
    }

    public ProductView getById(Long id){
        ProductView product = new ProductView();
        Product pr = productService.getProductById(id);

        product.setId(pr.getId());
        product.setName(pr.getName());
        product.setDescription(pr.getDescription());
        product.setPrice(pr.getPrice());
        product.setCreateDate(pr.getCreateDate());
        product.setQuantity(pr.getQuantity());
        List<ImageView> imageViewList = new ArrayList<>();
        imageViewList.add(ImageView.builder().image(pr.getImg()).build());

        product.setImages(imageViewList);
        return product;
    }
}
