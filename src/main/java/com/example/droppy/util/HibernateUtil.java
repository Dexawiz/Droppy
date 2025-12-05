package com.example.droppy.util;

import com.example.droppy.domain.entity.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Company.class);
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(Address.class);
            configuration.addAnnotatedClass(OrderItem.class);
            configuration.addAnnotatedClass(Order.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Exception e) {
            throw new RuntimeException("Failed creating SessionFactory: " + e.getMessage(), e);
        }
    }

    public static SessionFactory buildTestSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure("hibernate-test.cfg.xml");

            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Company.class);
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(Address.class);
            configuration.addAnnotatedClass(OrderItem.class);
            configuration.addAnnotatedClass(Order.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("Failed creating Test SessionFactory: " + e.getMessage(), e);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}