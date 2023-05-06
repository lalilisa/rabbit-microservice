package com.example.orderservice.repo;

import com.example.orderservice.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

    List<Orders> findOrdersByUserId(Long userId);
}
