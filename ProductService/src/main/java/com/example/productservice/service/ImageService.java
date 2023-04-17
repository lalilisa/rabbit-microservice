package com.example.productservice.service;

import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public class ImageService {
    @Autowired
    ImageRepo imageRepo;

    public List<Image> findAll() {
        return imageRepo.findAll();
    }

    @Query(value = "SELECT * FROM image WHERE productId=:productId", nativeQuery = true)
    public List<Image> getImageByProductId(Long productId) {
        return imageRepo.getImageByProductId(productId);
    }
}
