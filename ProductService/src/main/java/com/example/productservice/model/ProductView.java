package com.example.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductView {
    private Long id;

    private String name;

    private String description;

    private String createDate;

    private int price;

    private int quantity;
    private List<ImageView> images;
}
