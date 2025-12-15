package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.enums.Category;
import com.example.droppy.repository.dao.CompanyDao;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateCompanyDao implements CompanyDao {

    private final SessionFactory sessionFactory;

    public HibernateCompanyDao (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void save(Company company) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
        }
    }

    @Override
    public List<Company> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company", Company.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Company findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Company) session.createQuery("FROM Company WHERE id = :id", Company.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.getReference(Company.class, id);
            session.remove(company);
            tx.commit();
        }
    }

    @Override
    public List<Company> findByCategory(Category category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company WHERE category = :category", Company.class)
                    .setParameter("category", category)
                    .list();
        }
    }

    @Override
    public Company findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company WHERE name = :name", Company.class)
                    .setParameter("name", name)
                    .uniqueResult();
        }
    }
}
