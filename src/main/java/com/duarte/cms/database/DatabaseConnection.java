package com.duarte.cms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/customer_management";

    public static Connection getConnection() {
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
        String user = requireEnv("DB_USER");
        String password = requireEnv("DB_PASSWORD");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ligar à base de dados PostgreSQL", e);
        }
    }

    private static String requireEnv(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Variável de ambiente em falta: " + name );
        }

        return value;
    }
}
