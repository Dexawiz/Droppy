package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
            return session.createQuery("FROM Order WHERE id = :id", Order.class)
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
}
