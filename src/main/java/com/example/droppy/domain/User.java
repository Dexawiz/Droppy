package com.example.droppy.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String name;
    private String surname;
    private Role role;
    private String email;
    private String phone_number;
    private String card_number;
    private Status
}