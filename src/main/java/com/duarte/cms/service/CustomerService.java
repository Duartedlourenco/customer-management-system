package com.duarte.cms.service;

import com.duarte.cms.model.Customer;
import java.util.*;

public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private long nextId = 1L;

    public Customer createCustomer(String firstName, String lastName, String email, String phoneNumber) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        for (Customer customer : customers) {
            if (email.equalsIgnoreCase(customer.getEmail())) {
                throw new IllegalArgumentException("Email '" + email + "' already exists.");
            }
        }

        Customer newCustomer = new Customer(nextId++, firstName, lastName, email, phoneNumber);
        customers.add(newCustomer);
        return newCustomer;
    }

    public Customer getCustomerById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        for (Customer customer : customers) {
            if (id.equals(customer.getId())) {
                return customer;
            }
        }

        throw new NoSuchElementException("Customer with ID " + id + " not found.");
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer updateCustomer(Long id, String firstName, String lastName, String email, String phoneNumber) {
        Customer customer = getCustomerById(id);
        if (!customer.getEmail().equalsIgnoreCase(email)) {
            for (Customer existing : customers) {
                if (!existing.getId().equals(id) && email.equalsIgnoreCase(existing.getEmail())) {
                    throw new IllegalArgumentException("Email " + email + " is already in use by another customer.");
                }
            }
        }

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);

        return customer;
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customers.remove(customer);
    }
}
