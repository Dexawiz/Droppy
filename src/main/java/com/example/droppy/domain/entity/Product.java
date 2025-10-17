package com.example.droppy.domain.entity;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long companyId;
}
