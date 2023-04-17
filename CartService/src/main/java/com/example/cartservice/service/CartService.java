package com.example.cartservice.service;

import com.example.cartservice.entity.Cart;
import com.example.cartservice.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    CartRepo cartRepo;

    public CartService(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Query(value = "SELECT * FROM account WHERE userId=:userId and status=1", nativeQuery = true)
    public List<Cart> getCartByUserId(Long userId) {
        return cartRepo.getCartByUserId(userId);
    }

    public List<Cart> findAll() {
        return cartRepo.findAll();
    }

    public <S extends Cart> S save(S entity) {
        return cartRepo.save(entity);
    }
}
