package com.duarte.cms.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Customer {

    private final Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private final LocalDateTime createdAt;

    public Customer(Long id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.firstName = validateRequired(firstName, "First name");
        this.lastName = validateRequired(lastName, "Last name");
        this.email = validateRequired(email, "Email");
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
    }

    public Customer(Long id, String firstName, String lastName, String email, String phoneNumber, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.firstName = validateRequired(firstName, "First name");
        this.lastName = validateRequired(lastName, "Last name");
        this.email = validateRequired(email, "Email");
        this.phoneNumber = phoneNumber;
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date cannot be null");
    }

    private static String validateRequired(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }

        return value;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = validateRequired(firstName, "First name");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = validateRequired(lastName, "Last name");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateRequired(email, "Email");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
