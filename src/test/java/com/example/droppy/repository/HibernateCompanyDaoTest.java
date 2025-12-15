package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.hibernate.HibernateCompanyDao;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class HibernateCompanyDaoTest {
     private  HibernateCompanyDao companyDao;

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
        companyDao = new HibernateCompanyDao(testSessionFactory) {
            @Override
            public void save(Company company) {
                try (var session = testSessionFactory.openSession()) {
                    var tx = session.beginTransaction();
                    session.persist(company);
                    tx.commit();
                }
            }

            @Override
            public List<Company> findAll() {
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Company",Company.class).list();
                }
            }

            @Override
            public Company findById(Long id) {
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Company WHERE id = :id", Company.class)
                            .setParameter("id", id)
                            .uniqueResult();
                }
            }
            @Override
            public void delete(Long id) {
                try (var session = testSessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    Company company = session.getReference(Company.class, id);
                    session.remove(company);
                    tx.commit();
                }
            }

            @Override
            public List<Company> findByCategory(Category category){
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Company WHERE category = :category", Company.class)
                            .setParameter("category", category)
                            .list();
                }
            }
            @Override
            public Company findByName(String name) {
                try (var session = testSessionFactory.openSession()) {
                    return session.createQuery("FROM Company WHERE name = :name", Company.class)
                            .setParameter("name", name)
                            .uniqueResult();
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
    }

    @Test
    void testSaveAndFindById() {
        Company company = new Company();
        company.setName("Test Company");
        company.setCategory(Category.PIZZA);
        company.setAddress( "123 Test St");

        companyDao.save(company);

        Company retrieved = companyDao.findById(company.getId());
        assert retrieved != null;
        assert retrieved.getName().equals("Test Company");
        assert retrieved.getCategory() == Category.PIZZA;
    }

    @Test
    void testFindAll() {
        Company company1 = new Company();
        company1.setName("Company One");
        company1.setCategory(Category.DESSERT);
        company1.setAddress( "123 Test St");
        companyDao.save(company1);

        Company company2 = new Company();
        company2.setName("Company Two");
        company2.setCategory(Category.SUSHI);
        company2.setAddress( "456 Test Ave");
        companyDao.save(company2);

        List<Company> companies = companyDao.findAll();
        assert companies.size() == 2;
    }

    @Test
    void testDelete() {
        Company company = new Company();
        company.setName("To Be Deleted");
        company.setCategory(Category.DRINKS);
        company.setAddress("789 Test Blvd");
        companyDao.save(company);
        Long companyId = company.getId();
        companyDao.delete(companyId);
        Company deleted = companyDao
                .findById(companyId);
        assert deleted == null;
    }

    @Test
    void testFindByCategory() {
        Company company1 = new Company();
        company1.setName("Pizza Place");
        company1.setCategory(Category.PIZZA);
        company1.setAddress("123 Pizza St");
        companyDao.save(company1);

        Company company2 = new Company();
        company2.setName("Sushi Spot");
        company2.setCategory(Category.SUSHI);
        company2.setAddress("456 Sushi Ave");
        companyDao.save(company2);

        List<Company> pizzaCompanies = companyDao.findByCategory(Category.PIZZA);
        assert pizzaCompanies.size() == 1;
        assert pizzaCompanies.get(0).getName().equals("Pizza Place");
    }

    @Test
    void testFindByName() {
        Company company = new Company();
        company.setName("Unique Name Co");
        company.setCategory(Category.DESSERT);
        company.setAddress("789 Unique Rd");
        companyDao.save(company);
        Company retrieved = companyDao.findByName("Unique Name Co");
        assert retrieved != null;
        assert retrieved.getCategory() == Category.DESSERT;
    }
}
