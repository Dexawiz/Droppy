package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.DeliveryMethod;
import com.example.droppy.domain.enums.DriverStatus;
import com.example.droppy.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

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
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "role", nullable = false, columnDefinition = "user_role_enum")
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "card_number")
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "driver_status", columnDefinition = "driver_status_enum")
    private DriverStatus driverStatus;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "delivery_method", columnDefinition = "delivery_method_enum")
    private DeliveryMethod deliveryMethod;

    @Column(name = "password_hash")
    private String password;
}