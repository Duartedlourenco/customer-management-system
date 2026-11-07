package com.duarte.cms.service;

import com.duarte.cms.model.Customer;
import com.duarte.cms.repository.CustomerRepository;
import com.duarte.cms.repository.InMemoryCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerService service;

    @BeforeEach
    void setUp() {
        CustomerRepository repository = new InMemoryCustomerRepository();
        service = new CustomerService(repository);
    }

    @Test
    void testCreateCustomer(){
        Customer customer = service.createCustomer(
                "Duarte",
                "Lourenço",
                "duarte@email.com",
                "912345678"
        );

        assertNotNull(customer);
        assertEquals("Duarte", customer.getFirstName());
        assertEquals("Lourenço", customer.getLastName());
        assertEquals("duarte@email.com", customer.getEmail());
        assertEquals("912345678", customer.getPhoneNumber());
        assertEquals(1L, customer.getId());
    }

    @Test
    void testCreateCustomerWithNullEmail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.createCustomer(
                            "Duarte",
                            "Lourenço",
                            null,
                            "912345678"
                    );
                }
        );
    }

    @Test
    void testCreateCustomerWithEmptyEmail() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.createCustomer(
                            "Duarte",
                            "Lourenço",
                            "",
                            "912345678"
                    );
                }
        );
    }

    @Test
    void testDuplicateEmails() {
        service.createCustomer(
                "Duarte",
                "Lourenço",
                "duarte@email.com",
                "912345678"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.createCustomer(
                            "Duarte",
                            "Dias",
                            "duarte@email.com",
                            "987654321"
                    );
                }
        );
    }

    @Test
    void testUpdateCustomerWithDuplicateEmail() {
        Customer customer1 = service.createCustomer(
                "Duarte",
                "Lourenço",
                "duarte@email.com",
                "912345678"
        );

        service.createCustomer(
                "Daniel",
                "Silva",
                "daniel@email.com",
                "987654321"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateCustomer(
                        customer1.getId(),
                        "Dinis",
                        "Manuel",
                        "daniel@email.com",
                        "912345678")
        );

        assertEquals("Duarte", customer1.getFirstName());
        assertEquals("Lourenço", customer1.getLastName());
        assertEquals("duarte@email.com", customer1.getEmail());
        assertEquals("912345678", customer1.getPhoneNumber());
    }

    @Test
    void testGetCustomerById() {
        Customer customer = service.createCustomer(
                "Duarte",
                "Lourenço",
                "duarte@email.com",
                "912345678"
        );

        Customer found = service.getCustomerById(customer.getId());

        assertNotNull(found);
        assertEquals(customer.getId(), found.getId());
        assertEquals(customer.getEmail(), found.getEmail());
    }

    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(
                NoSuchElementException.class,
                () -> service.getCustomerById(1L)
        );
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = service.createCustomer(
                "Duarte",
                "Lourenço",
                "duarte@email.com",
                "912345678"
        );

        service.updateCustomer(customer.getId() ,"Daniel", "Silva", "daniel@email.com", "987654321");

        assertEquals("Daniel", customer.getFirstName());
        assertEquals("Silva", customer.getLastName());
        assertEquals("daniel@email.com", customer.getEmail());
        assertEquals("987654321", customer.getPhoneNumber());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = service.createCustomer(
                "Duarte",
                "Lourenço",
                "duarte@email.com",
                "912345678"
        );

        service.deleteCustomer(customer.getId());

        assertThrows(
                NoSuchElementException.class,
                () -> service.getCustomerById(customer.getId())
        );
    }
}
