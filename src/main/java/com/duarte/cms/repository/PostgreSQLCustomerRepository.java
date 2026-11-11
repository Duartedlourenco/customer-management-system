package com.duarte.cms.repository;

import com.duarte.cms.database.DatabaseConnection;
import com.duarte.cms.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PostgreSQLCustomerRepository implements CustomerRepository {

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            return insert(customer);
        }
        return update(customer);
    }

    private Customer insert(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone_number) " +
                "VALUES (?, ?, ?, ?) RETURNING id, created_at";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                    return new Customer(
                            id,
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getPhoneNumber(),
                            createdAt
                    );
                }

                throw new SQLException("Creating customer failed, no data returned.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving customer.", e);
        }
    }

    private Customer update(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone_number = ? " +
                "WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());
            statement.setLong(5, customer.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new NoSuchElementException("Customer with ID " + customer.getId() + " not found.");
            }

            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer.", e);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM customers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by ID: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM customers WHERE LOWER(email) = LOWER(?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by Email: " + email, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                customers.add(mapRow(resultSet));
            }

            return customers;

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving customers.", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer with ID: " + id, e);
        }
    }

    private Customer mapRow(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("phone_number"),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
