package com.example.droppy.repository.dao;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;

import java.util.List;

public interface OrderDao {
    void save(Order order);
    List<Order> findAll();
    Order findById(Long id);
    void delete(Long id);

    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    void updateOrderStatus(Long orderId, OrderStatus status);
    void updateDriverForOrder(Long orderId, Long driverId);
    Order findByStatusAndUser(OrderStatus status, User user);
    Order add(Order order);
    void update(Order order);
    Order updateOI (Order order);
    void updatePaymentMethod(Long orderId, MethodOfPayment methodOfPayment);
}