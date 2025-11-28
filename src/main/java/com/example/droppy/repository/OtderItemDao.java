package com.example.droppy.repository;

import com.example.droppy.domain.entity.OrderItem;

import java.util.List;

public interface OtderItemDao {
    void save(OrderItem orderItem);
    void delete(OrderItem orderItem);
    List<OrderItem> findAll();
    List<OrderItem> findByOrderId(Long orderId);
}
