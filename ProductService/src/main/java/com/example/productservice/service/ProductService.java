package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public List<Product> findAll(Sort sort) {
        return productRepo.findAll(sort);
    }

    public List<Product> findAllById(Iterable<Long> longs) {
        return productRepo.findAllById(longs);
    }

    public <S extends Product> S save(S entity) {
        return productRepo.save(entity);
    }

    public Optional<Product> findById(Long aLong) {
        return productRepo.findById(aLong);
    }

    public long count() {
        return productRepo.count();
    }

    @Query(value = "SELECT * FROM product WHERE id=:id", nativeQuery = true)
    public Product getProductById(Long id) {
        return productRepo.getProductById(id);
    }
}
