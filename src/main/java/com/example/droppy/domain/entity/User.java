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

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "card_number")
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_status")
    private DriverStatus driverStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod;

    @Column(name = "password_hash")
    private String password;
}