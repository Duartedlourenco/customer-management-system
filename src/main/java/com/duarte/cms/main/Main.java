package com.duarte.cms.main;

import com.duarte.cms.model.Customer;
import com.duarte.cms.repository.CustomerRepository;
import com.duarte.cms.database.DatabaseInitializer;
import com.duarte.cms.repository.PostgreSQLCustomerRepository;
import com.duarte.cms.service.CustomerService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static final CustomerRepository customerRepository = new PostgreSQLCustomerRepository();
    private static final CustomerService customerService = new CustomerService(customerRepository);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseInitializer.createTable();

        boolean running = true;

        while (running) {
            System.out.println("\n=== CUSTOMER MANAGEMENT SYSTEM ===");
            System.out.println("1. Create customer");
            System.out.println("2. Consult customer");
            System.out.println("3. List all customers");
            System.out.println("4. Update customer");
            System.out.println("5. Delete customer");
            System.out.println("6. Exit");
            System.out.print("> ");

            String input = scanner.nextLine();
            int option;

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number from 1 to 6.");
                continue;
            }

            try {
                switch (option) {
                    case 1 -> create();
                    case 2 -> consult();
                    case 3 -> list();
                    case 4 -> update();
                    case 5 -> delete();
                    case 6 -> {
                        running = false;
                        System.out.println("Exiting application... Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Choose between 1 and 6.");
                }
            } catch (RuntimeException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void create() {
        System.out.println("\n--- Create New Customer ---");
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone number: ");
        String phoneNumber = scanner.nextLine();

        try {
            Customer created = customerService.createCustomer(firstName, lastName, email, phoneNumber);
            System.out.println("Customer created successfully! Assigned ID: " + created.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating customer: " + e.getMessage());
        }
    }

    private static void consult() {
        System.out.println("\n--- Consult Customer by ID ---");
        System.out.print("Enter customer ID: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            Customer customer = customerService.getCustomerById(id);

            System.out.println("Customer found:");
            printCustomerDetails(customer);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Must be a number.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void list() {
        System.out.println("\n--- All Customers List ---");
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return;
        }

        for (Customer customer : customers) {
            printCustomerDetails(customer);
            System.out.println("-----------------------------------");
        }
    }

    private static void update() {
        System.out.println("\n--- Update Customer ---");
        System.out.print("Enter customer ID to update: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            customerService.getCustomerById(id);

            System.out.print("New first name: ");
            String firstName = scanner.nextLine();

            System.out.print("New last name: ");
            String lastName = scanner.nextLine();

            System.out.print("New email: ");
            String email = scanner.nextLine();

            System.out.print("New phone number: ");
            String phoneNumber = scanner.nextLine();

            Customer updated = customerService.updateCustomer(id, firstName, lastName, email, phoneNumber);
            System.out.println("Customer updated successfully!");
            printCustomerDetails(updated);

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Must be a number.");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void delete() {
        System.out.println("\n--- Delete Customer ---");
        System.out.print("Enter customer ID to delete: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            customerService.deleteCustomer(id);
            System.out.println("Customer with ID " + id + " deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Must be a number.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCustomerDetails(Customer c) {
        System.out.println("ID: " + c.getId());
        System.out.println("Name: " + c.getFirstName() + " " + c.getLastName());
        System.out.println("Email: " + c.getEmail());
        System.out.println("Phone: " + (c.getPhoneNumber() != null && !c.getPhoneNumber().isBlank() ? c.getPhoneNumber() : "N/A"));
        System.out.println("Created at: " + c.getCreatedAt());
    }
}