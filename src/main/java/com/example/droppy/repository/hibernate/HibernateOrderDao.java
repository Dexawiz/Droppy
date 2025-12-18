package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.OrderItem;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.repository.dao.OrderDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HibernateOrderDao implements OrderDao {

        private final SessionFactory sessionFactory;

        public HibernateOrderDao(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

    @Override
    public void save(Order order) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(order);
            tx.commit();
        }
    }

    @Override
    public List<Order> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Order", Order.class).list();
        }
    }

    @Override
    public Order findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Order o WHERE o.id = :id", Order.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, id);
            session.remove(order);
            tx.commit();
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Order WHERE customerId.id = :userId", Order.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Order o WHERE o.status = :status", Order.class)
                    .setParameter("status", status)
                    .list();
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, orderId);
            order.setStatus(status);
            session.merge(order);
            tx.commit();
        }
    }

    @Override
    public void updateDriverForOrder(Long orderId, Long driverId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, orderId);
            order.setDriverId(session.getReference(com.example.droppy.domain.entity.User.class, driverId));
            session.merge(order);
            tx.commit();
        }
    }

    @Override
    public Order findByStatusAndUser(OrderStatus status, User user) {
        try (Session session = this.sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Order o WHERE o.status = :status AND o.customerId = :user", Order.class)
                    .setParameter("status", status)
                    .setParameter("user", user)
                    .uniqueResult();
        }
    }

    @Override
    public Order add(Order orderItem) {
        Order managedOrder = null;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            managedOrder = findById(orderItem.getId());

            if (managedOrder == null) {
                // New order, simply persist
                managedOrder =  session.merge(orderItem);
            } else {
                // Existing order, update items
                List<OrderItem> oldItems = new ArrayList<>(managedOrder.getOrderItems());
                managedOrder.getOrderItems().clear();
                session.flush();
                managedOrder.getOrderItems().addAll(oldItems);

                for (OrderItem newItem : orderItem.getOrderItems()) {
                    newItem.setOrder(managedOrder);
                    managedOrder.getOrderItems().add(newItem);
                }
                managedOrder = session.merge(managedOrder);
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return managedOrder;
    }

    @Override
    public void update(Order order){
        try (Session session = this.sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            session.merge(order);
            tx.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Order updateOI(Order order) {
        Order managedOrder = null;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            managedOrder = findById( order.getId());

            if (managedOrder == null) {
                // New order, simply persist
                managedOrder = session.merge(order);
            } else {
                // Existing order, update items
                List<OrderItem> oldItems = new ArrayList<>(managedOrder.getOrderItems());

                managedOrder.getOrderItems().clear();
                session.flush();

                for (OrderItem oldItem : oldItems) {
                    if (order.getOrderItems().stream().anyMatch(o -> o.getId() != null && o.getId().equals(oldItem.getId()))) {
                        managedOrder.getOrderItems().add(oldItem);
                    } else {
                        session.remove(oldItem);
                    }
                }

                for (OrderItem newItem : order.getOrderItems()) {
                    // Check if the item is new or existing
                    if (newItem.getId() == null) {
                        newItem.setOrder(managedOrder);
                        managedOrder.getOrderItems().add(newItem);
                    } else {
                        // Existing item, update quantity
                        OrderItem existing = managedOrder.getOrderItems()
                                .stream()
                                .filter(oi -> oi.getId().equals(newItem.getId()))
                                .findFirst()
                                .orElse(null);
                        if (existing != null) {
                            existing.setQuantity(newItem.getQuantity());
                        }
                    }
                }

                managedOrder.setTotalPrice(order.getTotalPrice());

                managedOrder = session.merge(managedOrder);
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return managedOrder;
    }

    @Override
    public void updatePaymentMethod(Long orderId, MethodOfPayment methodOfPayment) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Order order = session.getReference(Order.class, orderId);
            order.setPaymentMethod(methodOfPayment);
            session.merge(order);
            tx.commit();
        }
    }
}
