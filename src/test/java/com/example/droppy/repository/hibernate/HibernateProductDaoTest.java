package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Product;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateProductDaoTest {

    private  HibernateProductDao productDao;
    private  static SessionFactory testSessionFactory;
    private  Company company;

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
        productDao = new HibernateProductDao(testSessionFactory) {
            @Override
            public void save(Product product) {
                try (var session = testSessionFactory.openSession()) {
                    var tx = session.beginTransaction();
                    session.persist(product);
                    tx.commit();
                }
            }

            @Override
            public List<Product> findAll() {
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Product", Product.class).list();
                }
            }

            @Override
            public Product findById(Long id) {
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Product WHERE id = :id", Product.class)
                            .setParameter("id", id)
                            .uniqueResult();
                }
            }

            @Override
            public List<Product> findByCompanyId(Long companyId) {
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Product WHERE company.id = :companyId", Product.class)
                            .setParameter("companyId", companyId)
                            .list();
                }
            }
            @Override
            public void delete(Long id) {
                try (var session = testSessionFactory.openSession()) {
                    var tx = session.beginTransaction();
                    Product product = session.getReference(Product.class, id);
                    session.remove(product);
                    tx.commit();
                }
            }
        };

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
    }

    @Test
    void save() {
        Company company;
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice( 19.99);
        product.setCompany( this.company);
        productDao.save(product);
        List<Product> products = productDao.findAll();
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    void findById() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice( 19.99);
        product.setCompany( this.company);
        productDao.save(product);
        Product retrieved = productDao.findById(product.getId());
        assertNotNull(retrieved);
        assertEquals("Test Product", retrieved.getName());
    }

    @Test
    void findAll() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice( 9.99);
        product1.setCompany( this.company);
        productDao.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice( 14.99);
        product2.setCompany( this.company);
        productDao.save(product2);

        List<Product> products = productDao.findAll();
        assertEquals(2, products.size());
    }

    @Test
    void findByCompanyId() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice( 9.99);
        product1.setCompany( this.company);
        productDao.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice( 14.99);
        product2.setCompany( this.company);
        productDao.save(product2);

        List<Product> products = productDao.findByCompanyId(this.company.getId());
        assertEquals(2, products.size());
    }

    @Test
    void delete() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice( 19.99);
        product.setCompany( this.company);
        productDao.save(product);
        Long productId = product.getId();
        assertNotNull(productDao.findById(productId));
        productDao.delete(productId);
        Product deleted = productDao.findById(productId);
        assertNull(deleted);
    }
}