package com.example.productservice.repository;

import com.example.productservice.entity.Image;
import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepo extends JpaRepository<Image, Long> {
    @Query(value = "SELECT * FROM image WHERE productId=:productId", nativeQuery = true)
    public List<Image> getImageByProductId(@Param("productId") Long productId);
}
