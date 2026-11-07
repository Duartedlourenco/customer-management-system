package com.duarte.cms.service;

import com.duarte.cms.model.Customer;
import com.duarte.cms.repository.CustomerRepository;

import java.util.*;

public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer createCustomer(String firstName, String lastName, String email, String phoneNumber) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email '" + email + "' already exists.");
        }

        Customer newCustomer = new Customer(null, firstName, lastName, email, phoneNumber);

        return repository.save(newCustomer);
    }

    public Customer getCustomerById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return repository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Customer with ID " + id + " not found."));
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer updateCustomer(Long id, String firstName, String lastName, String email, String phoneNumber) {
        Customer customer = getCustomerById(id);

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (!customer.getEmail().equalsIgnoreCase(email)
                && repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email " + email + " is already in use by another customer.");
        }

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);

        return repository.save(customer);
    }

    public void deleteCustomer(Long id) {
        getCustomerById(id);
        repository.deleteById(id);
    }
}
