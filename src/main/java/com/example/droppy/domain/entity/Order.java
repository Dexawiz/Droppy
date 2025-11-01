package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy =  jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customerId;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driverId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company companyId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;


    private Double totalPrice;
    private String deliveryFromAddress;
    private String deliveryToAddress;
    private LocalTime orderCreatedTime;
    private LocalTime estimatedDeliveryTime;

    @Enumerated(EnumType.STRING)
    private MethodOfPayment paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
