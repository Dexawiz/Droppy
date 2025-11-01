package com.example.droppy.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.droppy.domain.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PasswordMigration {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            List<User> users = session.createQuery("FROM User", User.class).list();

            for (User user : users) {
                if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$")) {
                    String hashed = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
                    // Обновляем только поле пароля через HQL, чтобы не трогать enum-поля в БД
                    session.createQuery("UPDATE User u SET u.password = :pw WHERE u.id = :id")
                            .setParameter("pw", hashed)
                            .setParameter("id", user.getId())
                            .executeUpdate();
                    System.out.println("✅ Захэширован пароль для: " + user.getEmail());
                }
            }

            tx.commit();
            System.out.println("Все пароли успешно обновлены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
