package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.domain.enums.MethodOfPayment;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateOrderDaoTest {

    private HibernateOrderDao orderDao;
    private User customer;
    private Company company;

    @BeforeAll
    static void init() {
        HibernateUtil.getSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }

    @BeforeEach
    void setUp() {
        orderDao = new HibernateOrderDao( HibernateUtil.buildTestSessionFactory());

        try (Session session = HibernateUtil.buildTestSessionFactory() .openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Order").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();
            tx.commit();
        }


        try (Session session = HibernateUtil.buildTestSessionFactory() .openSession()) {
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
            company.setAddress( "123 Test St");
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
        order.setOrderCreatedTime( java.time.LocalDateTime.now());
        order.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order);

        assertNotNull(order.getId(), "Saved order should have an ID");

        Order fetched = orderDao.findById(order.getId());
        assertNotNull(fetched);
        assertEquals(order.getTotalPrice(), fetched.getTotalPrice());
        assertEquals(OrderStatus.PENDING, fetched.getStatus());
    }

    @Test
    void findAll() {
        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setCompanyId(company);
        order1.setTotalPrice(10.0);
        order1.setStatus(OrderStatus.PENDING);
        order1.setDeliveryToAddress( "Address 1");
        order1.setDeliveryFromAddress( "Address 2");
        order1.setOrderCreatedTime( java.time.LocalDateTime.now());
        order1.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setCompanyId(company);
        order2.setTotalPrice(20.0);
        order2.setStatus(OrderStatus.ACCEPTED);
        order2.setDeliveryToAddress( "Address 1");
        order2.setDeliveryFromAddress( "Address 2");
        order2.setOrderCreatedTime( java.time.LocalDateTime.now());
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
        order.setTotalPrice(30.0);
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryToAddress( "Address 1");
        order.setDeliveryFromAddress( "Address 2");
        order.setOrderCreatedTime( java.time.LocalDateTime.now());
        order.setPaymentMethod(MethodOfPayment.CASH);
        orderDao.save(order);

        Long id = order.getId();
        orderDao.delete(id);

        Order fetched = orderDao.findById(id);
        assertNull(fetched, "Deleted order should not be found");
    }

    @Test
    void findByUserId() {
        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setCompanyId(company);
        order1.setStatus(OrderStatus.PENDING);
        order1.setDeliveryToAddress( "Address 1");
        order1.setDeliveryFromAddress( "Address 2");
        order1.setTotalPrice( 15.0);
        order1.setOrderCreatedTime( java.time.LocalDateTime.now());
        order1.setPaymentMethod( MethodOfPayment.CASH);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setCompanyId(company);
        order2.setStatus(OrderStatus.ACCEPTED);
        order2.setTotalPrice( 25.0);
        order2.setDeliveryToAddress( "Address 1");
        order2.setDeliveryFromAddress( "Address 2");
        order2.setOrderCreatedTime( java.time.LocalDateTime.now());
        order2.setPaymentMethod( MethodOfPayment.ONLINE);
        orderDao.save(order2);

        List<Order> orders = orderDao.findByUserId(customer.getId());
        assertEquals(2, orders.size());
    }

    @Test
    void findByStatus() {
        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setCompanyId(company);
        order1.setStatus(OrderStatus.PENDING);
        order1.setDeliveryFromAddress( "Address 2");
        order1.setDeliveryToAddress( "Address 1");
        order1.setTotalPrice( 35.0);
        order1.setOrderCreatedTime( java.time.LocalDateTime.now());
        order1.setPaymentMethod( MethodOfPayment.CASH);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setCompanyId(company);
        order2.setStatus(OrderStatus.ACCEPTED);
        order2.setDeliveryToAddress( "Address 1");
        order2.setDeliveryFromAddress( "Address 2");
        order2.setTotalPrice( 45.0);
        order2.setOrderCreatedTime( java.time.LocalDateTime.now());
        order2.setPaymentMethod( MethodOfPayment.ONLINE);
        orderDao.save(order2);

        List<Order> pendingOrders = orderDao.findByStatus(OrderStatus.PENDING);
        assertEquals(1, pendingOrders.size());
        assertEquals(OrderStatus.PENDING, pendingOrders.get(0).getStatus());
    }
}
