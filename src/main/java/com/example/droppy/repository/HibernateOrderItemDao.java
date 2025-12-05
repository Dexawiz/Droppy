package com.example.droppy.repository;

import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class HibernateOrderItemDao implements OrderItemDao {

    private final SessionFactory sessionFactory;

    public HibernateOrderItemDao (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(OrderItem orderItem) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.persist(orderItem);
            tx.commit();
        }
    }

    @Override
    public void delete(OrderItem orderItem) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.remove(orderItem);
            tx.commit();
        }
    }

    @Override
    public List<OrderItem> findAll() {
        try(Session session =  HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM OrderItem", OrderItem.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM OrderItem oi WHERE oi.order.id = :orderId", OrderItem.class)
                    .setParameter("orderId", orderId)
                    .list();
        }
    }
}
