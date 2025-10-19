package com.example.droppy.domain.entity;

import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Long customerId;
    private Long driverId;
    private Long companyId;
    private List<Product> products;
    private Double totalPrice;
    private String deliveryFromAddress;
    private String deliveryToAddress;
    private LocalTime orderCreatedTime;
    private LocalTime estimatedDeliveryTime;
    private MethodOfPayment paymentMethod;
    private OrderStatus status;
}
