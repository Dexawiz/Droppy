package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.DeliveryMethod;
import com.example.droppy.domain.enums.DriverStatus;
import com.example.droppy.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy =  jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String phoneNumber;
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;
    private String password;
}