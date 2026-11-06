package com.duarte.cms.repository;

import com.duarte.cms.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository{

    private final List<Customer> customers = new ArrayList<>();
    private long nextId = 1L;

    @Override
    public Customer save(Customer customer) {
        for (Customer existing : customers) {
            boolean isDifferentCustomer = customer.getId() == null || !existing.getId().equals(customer.getId());

            if(isDifferentCustomer && existing.getEmail().equalsIgnoreCase(customer.getEmail())) {
                throw new IllegalArgumentException("Email " + customer.getEmail() + " is already in use by another customer.");
            }
        }

        if (customer.getId() == null) {
            Customer newCustomer = new Customer(
                    nextId++,
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhoneNumber()
            );
            customers.add(newCustomer);
            return newCustomer;
        }

        Customer existingCustomer = findById(customer.getId()).orElseThrow(() ->
                new NoSuchElementException("Customer with ID " + customer.getId() + " not found."));

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());

        return existingCustomer;
    }

    @Override
    public Optional<Customer> findById(Long id) {

        for (Customer customer : customers) {
            if (id.equals(customer.getId())) {
                return Optional.of(customer);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        for (Customer customer : customers) {
            if (email.equalsIgnoreCase(customer.getEmail())) {
                return Optional.of(customer);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            customers.removeIf(customer -> id.equals(customer.getId()));
        }
    }
}
