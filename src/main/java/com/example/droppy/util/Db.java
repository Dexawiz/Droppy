package com.example.droppy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private static final String URL  =
            System.getenv().getOrDefault("DB_URL",  "jdbc:postgresql://localhost:5432/droppy");
    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "droppy");
    private static final String PASS =
            System.getenv().getOrDefault("DB_PASS", "admin");

    static {
        try { Class.forName("org.postgresql.Driver"); } catch (Exception ignored) {}
    }
    private Db() {}
    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
