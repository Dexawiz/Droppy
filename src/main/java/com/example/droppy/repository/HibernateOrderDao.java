package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HibernateOrderDao implements OrderDao {

    @Override
    public void save(Order order) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(order);
            tx.commit();
        }
    }

    @Override
    public List<Order> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order", Order.class).list();
        }
    }

    @Override
    public Order findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Order o WHERE o.id = :id", Order.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, id);
            session.remove(order);
            tx.commit();
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order WHERE customerId = :userId", Order.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Order o WHERE o.status = :status", Order.class)
                    .setParameter("status", status)
                    .list();
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, orderId);
            order.setStatus(status);
            session.merge(order);
            tx.commit();
        }
    }

    @Override
    public void updateDriverForOrder(Long orderId, Long driverId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, orderId);
            order.setDriverId(session.getReference(com.example.droppy.domain.entity.User.class, driverId));
            session.merge(order);
            tx.commit();
        }
    }

    @Override
    public Order findByStatusAndUser(OrderStatus status, User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Order o WHERE o.status = :status AND o.customerId = :user", Order.class)
                    .setParameter("status", status)
                    .setParameter("user", user)
                    .uniqueResult();
        }
    }

    @Override
    public Order add(Order ordIerm) {
        Order managedOrder = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            managedOrder = findById(ordIerm.getId());

            if (managedOrder == null) {

                managedOrder = (Order) session.merge(ordIerm);
            } else {
                List<OrderItem> oldItems = new ArrayList<>(managedOrder.getOrderItems());

                managedOrder.getOrderItems().clear();
                session.flush();
                managedOrder.getOrderItems().addAll(oldItems);

                for (OrderItem newItem : ordIerm.getOrderItems()) {
                    newItem.setOrder(managedOrder);
                    managedOrder.getOrderItems().add(newItem);
                }

                managedOrder = (Order) session.merge(managedOrder);
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return managedOrder;
    }

    @Override
    public void update(Order order) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(order);
            tx.commit();
        }
    }
}
