package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class HibernateUserDaoTest {

    private  HibernateUserDao userDao;
    private  static SessionFactory testSessionFactory;

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
        userDao = new HibernateUserDao(testSessionFactory);

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
    }

    @Test
    void save() {
        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);
        userDao.save(user);
        User retrievedUser = userDao.findById(user.getId());
        assert retrievedUser != null;
        assert retrievedUser.getEmail().equals("test@example.com");

    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setName("Test1");
        user1.setSurname("User1");
        user1.setEmail("zxc@zxc");
        user1.setPassword("password1");
        user1.setRole(Role.CUSTOMER);
        userDao.save(user1);
        User user2 = new User();
        user2.setName("Test2");
        user2.setSurname("User2");
        user2.setEmail("asd@asd");
        user2.setPassword("password2");
        user2.setRole(Role.ADMIN);
        userDao.save(user2);
        List<User> users = userDao.findAll();
        assert users.size() == 2;

    }

    @Test
    void findById() {
        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("avc@avc");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);
        userDao.save(user);
        User retrievedUser = userDao.findById(user.getId());
        assert retrievedUser != null;
        assert retrievedUser.getEmail().equals("avc@avc");
    }

    @Test
    void delete() {
        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("qwe@qwe");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);

        userDao.save(user);
        Long id = user.getId();

        userDao.delete(id);

        User deletedUser = userDao.findById(id);
        assert deletedUser == null;
    }

    @Test
    void findByEmail() {
        User user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("email@email");
        user.setPassword("password");
        user.setRole(Role.CUSTOMER);
        userDao.save(user);
        User retrievedUser = userDao.findByEmail("email@email");
        assert retrievedUser != null;
        assert retrievedUser.getName().equals("Test");
    }

    @Test
    void findByRole() {
        User user1 = new User();
        user1.setName("Test1");
        user1.setSurname("User1");
        user1.setEmail("role1@role");
        user1.setPassword("password1");
        user1.setRole(Role.CUSTOMER);
        userDao.save(user1);
        User user2 = new User();
        user2.setName("Test2");
        user2.setSurname("User2");
        user2.setEmail("role2@role");
        user2.setPassword("password2");
        user2.setRole(Role.CUSTOMER);
        userDao.save(user2);
        User user3 = new User();
        user3.setName("Test3");
        user3.setSurname("User3");
        user3.setEmail("role3@role");
        user3.setPassword("password3");
        user3.setRole(Role.ADMIN);
        userDao.save(user3);
        List<User> customers = userDao.findByRole(Role.CUSTOMER);
        assert customers.size() == 2;
    }
}