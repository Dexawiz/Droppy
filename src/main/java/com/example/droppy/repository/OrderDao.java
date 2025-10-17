package com.example.droppy.repository;

import com.example.droppy.domain.entity.Order;

import java.util.List;

public interface OrderDao {
    void save(Order order);
    List<Order> findAll();
    Order findById(Long id);
    void delete(Long id);


    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);
}
