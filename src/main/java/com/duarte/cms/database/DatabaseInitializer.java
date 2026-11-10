package com.duarte.cms.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void main(String[] args) {
        createTable();
        System.out.println("Customers table ready!");
    }

    public static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS customers (
                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    first_name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) NOT NULL,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    phone_number VARCHAR(20),
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                );
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Error creating customers table.", e);
        }
    }
}
