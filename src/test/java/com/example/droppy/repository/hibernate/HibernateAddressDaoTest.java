package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.Address;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateAddressDaoTest {

    private HibernateAddressDao addressDao;
    private User user;

    // test factory to use for all operations in this test
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
        // create an anonymous subclass of HibernateAddressDao that uses the testSessionFactory directly
        addressDao = new HibernateAddressDao(testSessionFactory) {
            @Override
            public void save(Address address) {
                try (Session session = testSessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    session.persist(address);
                    tx.commit();
                }
            }

            @Override
            public List<Address> findAll() {
                try (Session session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Address", Address.class).list();
                }
            }

            @Override
            public Address findById(Long id) {
                try (Session session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Address WHERE id = :id", Address.class)
                            .setParameter("id", id)
                            .uniqueResult();
                }
            }

            @Override
            public List<Address> findByUserId(Long userId) {
                try (Session session = testSessionFactory.openSession()) {
                    User u = session.createQuery("FROM User WHERE id = :userId", User.class)
                            .setParameter("userId", userId)
                            .uniqueResult();
                    if (u != null && u.getAddress() != null) {
                        return List.of(u.getAddress());
                    } else {
                        return List.of();
                    }
                }
            }

            @Override
            public void delete(Long id) {
                try (Session session = testSessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    Address a = session.getReference(Address.class, id);
                    if (a != null) {
                        session.remove(a);
                    }
                    tx.commit();
                }
            }
        };

        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            // clear tables in a stable order
            session.createQuery("DELETE FROM OrderItem").executeUpdate();
            session.createQuery("DELETE FROM Order").executeUpdate();
            session.createQuery("DELETE FROM Address").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();
            session.createQuery("DELETE FROM Product").executeUpdate();

            tx.commit();
        }

        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            user = new User();
            user.setName("Test");
            user.setSurname("User");
            user.setEmail("test@example.com");
            user.setPassword("password");
            user.setRole(Role.CUSTOMER);

            session.persist(user);
            tx.commit();
        }
    }

    @Test
    void testSaveAndFindById() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Testville");
        address.setCountry("Testland");
        address.setPostalCode("12345");

        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(address);
            user.setAddress(address);
            user = session.merge(user); // присваиваем результат merge
            tx.commit();
        }

        Address retrieved = addressDao.findById(address.getId());
        assertNotNull(retrieved);
        assertEquals("123 Main St", retrieved.getStreet());
        assertEquals("Testville", retrieved.getCity());
        assertEquals("Testland", retrieved.getCountry());
        assertEquals("12345", retrieved.getPostalCode());
    }

    @Test
    void testFindByUserId() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Testville");
        address.setCountry("Testland");
        address.setPostalCode("12345");

        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(address);
            user.setAddress(address);
            user = session.merge(user);
            tx.commit();
        }

        List<Address> addresses = addressDao.findByUserId(user.getId());
        assertFalse(addresses.isEmpty());

        Address retrieved = addresses.get(0);
        assertNotNull(retrieved);
        assertEquals("123 Main St", retrieved.getStreet());
        assertEquals("Testville", retrieved.getCity());
        assertEquals("Testland", retrieved.getCountry());
        assertEquals("12345", retrieved.getPostalCode());
    }

    @Test
    void testDelete() {
        Address address = new Address();
        address.setStreet("456 Another St");
        address.setCity("Cityville");
        address.setCountry("Countryland");
        address.setPostalCode("67890");

        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(address);
            user.setAddress(address);
            user = session.merge(user);
            tx.commit();
        }

        Long addressId = address.getId();
        assertNotNull(addressDao.findById(addressId));

        addressDao.delete(addressId);

        Address deleted = addressDao.findById(addressId);
        assertNull(deleted);
    }

    @Test
    void testFindAll() {
        Address address1 = new Address();
        address1.setStreet("123 Main St");
        address1.setCity("Testville");
        address1.setCountry("Testland");
        address1.setPostalCode("12345");

        Address address2 = new Address();
        address2.setStreet("456 Another St");
        address2.setCity("Cityville");
        address2.setCountry("Countryland");
        address2.setPostalCode("67890");

        try (Session session = testSessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(address1);
            session.persist(address2);
            tx.commit();
        }

        List<Address> addresses = addressDao.findAll();
        assertEquals(2, addresses.size());
    }

    @Test
    void testFindByIdNotFound() {
        Address address = addressDao.findById(9999L);
        assertNull(address);
    }
}
