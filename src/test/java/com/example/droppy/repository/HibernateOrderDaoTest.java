package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.*;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateOrderDaoTest {

    private HibernateOrderDao orderDao;
    private User customer;
    private Company company;

    // единый SessionFactory для всех тестов
    private static SessionFactory testSessionFactory;

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

        // создаем анонимный DAO, как в тесте Address
        orderDao = new HibernateOrderDao(testSessionFactory) {
            @Override
            public void save(Order order) {
                try (Session session = testSessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    session.persist(order);
                    tx.commit();
                }
            }

            @Override
            public List<Order> findAll() {
                try (Session session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Order", Order.class).list();
                }
            }

            @Override
            public Order findById(Long id) {
                try (Session session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Order WHERE id = :id", Order.class)
                            .setParameter("id", id)
                            .uniqueResult();
                }
            }

            @Override
            public void delete(Long id) {
                try (Session session = testSessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    Order o = session.getReference(Order.class, id);
                    if (o != null) {
                        session.remove(o);
                    }
                    tx.commit();
                }
            }

            @Override
            public List<Order> findByUserId(Long userId) {
                try (Session session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Order WHERE customerId.id = :cid", Order.class)
                            .setParameter("cid", userId)
                            .list();
                }
            }

            @Override
            public List<Order> findByStatus(OrderStatus status) {
                try (Session session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Order WHERE status = :st", Order.class)
                            .setParameter("st", status)
                            .list();
                }
            }
        };

        // очищаем таблицы
        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM OrderItem").executeUpdate();
            session.createQuery("DELETE FROM Order").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();
            tx.commit();
        }

        // создаем customer и company
        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            customer = new User();
            customer.setName("Test");
            customer.setSurname("User");
            customer.setEmail("test@example.com");
            customer.setRole(Role.CUSTOMER);
            customer.setPassword("password");
            session.persist(customer);

            company = new Company();
            company.setName("Test Company");
            company.setAddress("123 Test St");
            company.setCategory(Category.PIZZA);
            session.persist(company);

            tx.commit();
        }
    }

    @Test
    void saveAndFindById() {
        Order order = new Order();
        order.setCustomerId(customer);
        order.setCompanyId(company);
        order.setDeliveryFromAddress("From A");
        order.setDeliveryToAddress("To B");
        order.setTotalPrice(50.0);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderCreatedTime(java.time.LocalDateTime.now());
        order.setPaymentMethod(MethodOfPayment.CASH);

        orderDao.save(order);

        assertNotNull(order.getId());

        Order fetched = orderDao.findById(order.getId());
        assertNotNull(fetched);
        assertEquals(50.0, fetched.getTotalPrice());
        assertEquals(OrderStatus.PENDING, fetched.getStatus());
    }

    @Test
    void findAll() {
        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setCompanyId(company);
        order1.setDeliveryFromAddress("A");
        order1.setDeliveryToAddress("B");
        order1.setTotalPrice(10.0);
        order1.setStatus(OrderStatus.PENDING);
        order1.setOrderCreatedTime(java.time.LocalDateTime.now());
        order1.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setCompanyId(company);
        order2.setDeliveryFromAddress("A");
        order2.setDeliveryToAddress("B");
        order2.setTotalPrice(20.0);
        order2.setStatus(OrderStatus.ACCEPTED);
        order2.setOrderCreatedTime(java.time.LocalDateTime.now());
        order2.setPaymentMethod(MethodOfPayment.ONLINE);
        orderDao.save(order2);

        List<Order> orders = orderDao.findAll();
        assertEquals(2, orders.size());
    }

    @Test
    void delete() {
        Order order = new Order();
        order.setCustomerId(customer);
        order.setCompanyId(company);
        order.setDeliveryFromAddress("A");
        order.setDeliveryToAddress("B");
        order.setTotalPrice(30.0);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderCreatedTime(java.time.LocalDateTime.now());
        order.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order);

        Long id = order.getId();
        orderDao.delete(id);

        assertNull(orderDao.findById(id));
    }

    @Test
    void findByUserId() {
        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setCompanyId(company);
        order1.setDeliveryFromAddress("A");
        order1.setDeliveryToAddress("B");
        order1.setTotalPrice(15.0);
        order1.setStatus(OrderStatus.PENDING);
        order1.setOrderCreatedTime(java.time.LocalDateTime.now());
        order1.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setCompanyId(company);
        order2.setDeliveryFromAddress("A");
        order2.setDeliveryToAddress("B");
        order2.setTotalPrice(25.0);
        order2.setStatus(OrderStatus.ACCEPTED);
        order2.setOrderCreatedTime(java.time.LocalDateTime.now());
        order2.setPaymentMethod(MethodOfPayment.ONLINE);
        orderDao.save(order2);

        List<Order> orders = orderDao.findByUserId(customer.getId());
        assertEquals(2, orders.size());
    }

    @Test
    void findByStatus() {
        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setCompanyId(company);
        order1.setDeliveryFromAddress("A");
        order1.setDeliveryToAddress("B");
        order1.setTotalPrice(35.0);
        order1.setStatus(OrderStatus.PENDING);
        order1.setOrderCreatedTime(java.time.LocalDateTime.now());
        order1.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setCompanyId(company);
        order2.setDeliveryFromAddress("A");
        order2.setDeliveryToAddress("B");
        order2.setTotalPrice(45.0);
        order2.setStatus(OrderStatus.ACCEPTED);
        order2.setOrderCreatedTime(java.time.LocalDateTime.now());
        order2.setPaymentMethod(MethodOfPayment.ONLINE);
        orderDao.save(order2);

        List<Order> pendingOrders = orderDao.findByStatus(OrderStatus.PENDING);
        assertEquals(1, pendingOrders.size());
        assertEquals(OrderStatus.PENDING, pendingOrders.get(0).getStatus());
    }
}
