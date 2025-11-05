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
            Transaction tx = session.beginTransaction();
            if (user.getId() == null) {
                session.persist(user);
            } else {
                session.merge(user);
            }
            tx.commit();
        }
    }

    @Override
    public List<User> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
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
            Transaction tx = session.beginTransaction();
            User user = session.getReference(User.class, id);
            session.remove(user);
            tx.commit();
        }
    }

    @Override
    public void create(String name, String surname, String email, String password, Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Boolean exists = session.createSelectionQuery(
                            "select count(u) > 0 from User u where u.email = :email", Boolean.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (Boolean.TRUE.equals(exists)) {
                throw new IllegalArgumentException("User with email " + email + " already exists.");
            }

            User newUser = new User();
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(role);

            session.persist(newUser);
            tx.commit();
        }
    }

    @Override
    public User findByEmail(String email) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            SelectionQuery<User> query = session.createSelectionQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }

    @Override
    public List<User> findByRole(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE role = :role", User.class)
                    .setParameter("role", role)
                    .list();
        }
    }
}
