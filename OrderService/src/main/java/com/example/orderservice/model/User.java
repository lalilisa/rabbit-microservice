package com.example.orderservice.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class User implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String address;
    private String phone;
}
