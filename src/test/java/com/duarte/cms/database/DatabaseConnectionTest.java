package com.duarte.cms.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Só corre se DB_USER estiver definido (ou seja, se houver uma BD configurada)
@EnabledIfEnvironmentVariable(named = "DB_USER", matches = ".+")
class DatabaseConnectionTest {

    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }
}
