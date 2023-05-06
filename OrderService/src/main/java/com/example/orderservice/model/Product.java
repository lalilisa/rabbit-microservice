package com.example.orderservice.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Product implements Serializable {
    private Long id;

    private String name;

    private String description;

    private String createDate;

    private int price;

    private int quantity;

    private String img;
}
