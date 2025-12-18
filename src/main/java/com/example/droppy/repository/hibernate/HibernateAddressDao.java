package com.example.droppy.repository.hibernate;

import com.example.droppy.domain.entity.Address;
import com.example.droppy.domain.entity.User;
import com.example.droppy.repository.dao.AddressDao;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateAddressDao  implements AddressDao {

    private final SessionFactory sessionFactory;

    public HibernateAddressDao (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Address address) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(address);
            tx.commit();
        }
    }

    @Override
    public List<Address> findAll() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Address", Address.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Address findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return (Address) session.createQuery("FROM Address WHERE id = :id", Address.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public List<Address> findByUserId(Long userId) {
        try(Session session = sessionFactory.openSession()){
            User user = session.createQuery("FROM User WHERE id = :userId", User.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            if(user != null && user.getAddress() != null){
                return List.of(user.getAddress());
            } else {
                return List.of();
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Address address = session.getReference(Address.class, id);
            session.remove(address);
            tx.commit();
        }
    }
}
