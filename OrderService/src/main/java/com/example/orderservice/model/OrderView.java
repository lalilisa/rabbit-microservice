package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderView implements Serializable {
    private String name;
    private String address;
    private String phonenumber;
    private String username;
    List<OrderItemVIew> orderItemVIews;
}
