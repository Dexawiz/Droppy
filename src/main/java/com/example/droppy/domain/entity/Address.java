package com.example.droppy.domain.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String street;
    @Column(name = "postal_code")
    private String postalCode;
    private String country;

}
