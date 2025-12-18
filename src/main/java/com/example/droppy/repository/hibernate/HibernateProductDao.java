package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.Product;
import com.example.droppy.repository.dao.ProductDao;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateProductDao implements ProductDao {
    private final SessionFactory sessionFactory;

    public HibernateProductDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Product product) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(product);
            tx.commit();
        }
    }

    @Override
    public Product findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Product WHERE id = :id", Product.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public List<Product> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Product", Product.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Product> findByCompanyId(Long companyId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Product p WHERE p.company.id = :companyId", Product.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Product product = session.getReference(Product.class, id);
            session.remove(product);
            tx.commit();
        }
    }
}
