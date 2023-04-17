package com.example.cartservice.repository;

import com.example.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT * FROM account WHERE userId=:userId", nativeQuery = true)
    public List<Cart> getCartByUserId(@Param("userId") Long userId);
}
