package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.DeliveryMethod;
import com.example.droppy.domain.enums.DriverStatus;
import com.example.droppy.domain.enums.Role;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String surname;
    private Role role;
    private String email;
    private String phoneNumber;
    private String cardNumber;
    private DriverStatus driverStatus;
    private DeliveryMethod deliveryMethod;
    private String password;
}