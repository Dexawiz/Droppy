package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.Category;
import lombok.Data;

import java.time.LocalTime;

@Data
public class Company {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalTime workStart;
    private LocalTime workEnd;
    private Category category;
}
