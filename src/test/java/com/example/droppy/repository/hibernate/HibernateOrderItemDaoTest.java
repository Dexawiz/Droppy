package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.*;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateOrderItemDaoTest {

    private  HibernateOrderItemDao orderItemDao;
    private  static SessionFactory testSessionFactory;
    private Company company;
    private Product product;
    private Order order;
    private User customer;

    @BeforeAll
    static void init() {
        testSessionFactory = HibernateUtil.buildTestSessionFactory();
    }
    @AfterAll
    static void tearDown() {
        if (testSessionFactory != null) {
            testSessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        orderItemDao = new HibernateOrderItemDao(testSessionFactory);


        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            session.createQuery("DELETE FROM OrderItem").executeUpdate();
            session.createQuery("DELETE FROM Order").executeUpdate();
            session.createQuery("DELETE FROM Address").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();
            session.createQuery("DELETE FROM Product").executeUpdate();

            tx.commit();
        }

        try (Session session = testSessionFactory.openSession()){
            company = new Company();
            company.setName("Test Company");
            company.setAddress( "123 Test St");
            company.setWorkStart(LocalTime.parse("09:00"));
            company.setWorkEnd(LocalTime.parse("21:00"));
            company.setCategory( Category.PIZZA);
            Transaction tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
        }

        try (Session session = testSessionFactory.openSession()){
            product = new Product();
            product.setName("Test Product");
            product.setDescription("Test Description");
            product.setPrice(9.99);
            product.setCompany(company);
            Transaction tx = session.beginTransaction();
            session.persist(product);
            tx.commit();
        }

        try (Session session = testSessionFactory.openSession()){
            customer = new User();
            customer.setName("Test");
            customer.setSurname("User");
            customer.setEmail("test@example.com");
            customer.setRole(Role.CUSTOMER);
            customer.setPassword("password");
            Transaction tx = session.beginTransaction();
            session.persist(customer);
            tx.commit();
        }

        try  (Session session = testSessionFactory.openSession()){
            order = new Order();
            order.setCustomerId(customer);
            order.setCompanyId( company);
            order.setDeliveryFromAddress( "From A");
            order.setDeliveryToAddress( "To B");
            order.setOrderCreatedTime( LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);
            order.setPaymentMethod( MethodOfPayment.CASH);
            order.setTotalPrice(0.0);
            Transaction tx = session.beginTransaction();
            session.persist(order);
            tx.commit();
        }
    }


    @Test
    void save() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        orderItem.setPricePerItem(19.99);
        orderItem.setProduct( product);
        orderItem.setOrder( order);
        orderItemDao.save(orderItem);
        assertNotNull(orderItem.getId());
        assert orderItem.getQuantity() == 2;
    }

    @Test
    void delete() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        orderItem.setPricePerItem(19.99);
        orderItem.setProduct( product);
        orderItem.setOrder( order);
        orderItemDao.save(orderItem);
        Long id = orderItem.getId();
        assertNotNull(id);

        orderItemDao.delete(orderItem);

        List<OrderItem> items = orderItemDao.findAll();
        assertTrue(items.stream().noneMatch(oi -> oi.getId().equals(id)));
    }

    @Test
    void findAll() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(1);
        orderItem1.setPricePerItem(9.99);
        orderItem1.setProduct( product);
        orderItem1.setOrder( order);
        orderItemDao.save(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(3);
        orderItem2.setPricePerItem(29.97);
        orderItem2.setProduct( product);
        orderItem2.setOrder( order);
        orderItemDao.save(orderItem2);

        List<OrderItem> items = orderItemDao.findAll();
        assertEquals(2, items.size());
    }

    @Test
    void findByOrderId() {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setQuantity(1);
        orderItem1.setPricePerItem(9.99);
        orderItem1.setProduct( product);
        orderItem1.setOrder( order);
        orderItemDao.save(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setQuantity(3);
        orderItem2.setPricePerItem(29.97);
        orderItem2.setProduct( product);
        orderItem2.setOrder( order);
        orderItemDao.save(orderItem2);

        List<OrderItem> items = orderItemDao.findByOrderId(order.getId());
        assertEquals(2, items.size());
    }
}