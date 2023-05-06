package com.example.orderservice.repo;

import com.example.orderservice.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {

    List<OrderDetail> findByOrderId(Long orderId);
}
