package com.example.cartservice.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Cart {

    private Long id;

    private String productName;

    private Long productId;

    private String userName;

    private String username;

    private int quantity;

    private int status;
}
