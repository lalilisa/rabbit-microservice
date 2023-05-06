package com.example.orderservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderItemVIew implements Serializable {

    private Long productId;
    private String productName;
    private Integer price;
    private String img;
    private Integer quantity;
}
