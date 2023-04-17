package com.example.productservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String createDate;

    private int price;

    private int quantity;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images;

}
