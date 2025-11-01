package com.example.droppy.repository;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class HibernateUserDao  implements  UserDao {

    @Override
    public void save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.getTransaction();
            session.persist(user);
            tx.commit();
        }
    }

    @Override
    public List<User> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (User) session.createQuery("FROM User WHERE id = :id", User.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.getTransaction();
            User user = session.getReference(User.class, id);
            session.remove(user);
            tx.commit();
        }
    }

    @Override
    public void create(String name, String surname, String email, String password, Role role) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        save(user);
    }

    @Override
    public User findByEmail(String email) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            SelectionQuery<User> query = session.createSelectionQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }
}
