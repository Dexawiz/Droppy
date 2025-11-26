package com.example.droppy.repository;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.OrderStatus;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateOrderDaoTest {

    private HibernateOrderDao orderDao;

    @BeforeAll
    static void init() {
        // Инициализация Hibernate для тестовой H2 базы
        HibernateUtil.getSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }

    @BeforeEach
    void setUp() {
        orderDao = new HibernateOrderDao();

        // Очистка таблицы orders перед каждым тестом
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Order").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void saveAndFindById() {
        User customer = new User();
        customer.setId(1L);

        Order order = new Order();
        order.setCustomerId(customer);
        order.setDeliveryFromAddress("From A");
        order.setDeliveryToAddress("To B");
        order.setTotalPrice(50.0);
        order.setStatus(OrderStatus.PENDING);

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
        order1.setTotalPrice(10.0);
        order1.setStatus(OrderStatus.PENDING);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setTotalPrice(20.0);
        order2.setStatus(OrderStatus.ACCEPTED);
        orderDao.save(order2);

        List<Order> orders = orderDao.findAll();
        assertEquals(2, orders.size());
    }

    @Test
    void delete() {
        Order order = new Order();
        order.setTotalPrice(30.0);
        order.setStatus(OrderStatus.PENDING);
        orderDao.save(order);

        Long id = order.getId();
        orderDao.delete(id);

        Order fetched = orderDao.findById(id);
        assertNull(fetched, "Deleted order should not be found");
    }

    @Test
    void findByUserId() {
        User customer = new User();
        customer.setId(1L);

        Order order1 = new Order();
        order1.setCustomerId(customer);
        order1.setStatus(OrderStatus.PENDING);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setCustomerId(customer);
        order2.setStatus(OrderStatus.ACCEPTED);
        orderDao.save(order2);

        List<Order> orders = orderDao.findByUserId(1L);
        assertEquals(2, orders.size());
    }

    @Test
    void findByStatus() {
        Order order1 = new Order();
        order1.setStatus(OrderStatus.PENDING);
        orderDao.save(order1);

        Order order2 = new Order();
        order2.setStatus(OrderStatus.ACCEPTED);
        orderDao.save(order2);

        List<Order> pendingOrders = orderDao.findByStatus(OrderStatus.PENDING);
        assertEquals(1, pendingOrders.size());
        assertEquals(OrderStatus.PENDING, pendingOrders.get(0).getStatus());
    }
}
