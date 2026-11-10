package com.duarte.cms.repository;

import com.duarte.cms.database.DatabaseInitializer;
import com.duarte.cms.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "DB_USER", matches = ".+")
class PostgreSQLCustomerRepositoryTest {

    private final CustomerRepository repository = new PostgreSQLCustomerRepository();
    private final List<Long> createdIds = new ArrayList<>();

    @BeforeAll
    static void createTable() {
        DatabaseInitializer.createTable();
    }

    @AfterEach
    void cleanUp() {
        createdIds.forEach(repository::deleteById);
        createdIds.clear();
    }

    private Customer saveNewCustomer() {
        String email = "test-" + UUID.randomUUID() + "@email.com";
        Customer saved = repository.save(new Customer(null, "Ana", "Silva", email, "912345678"));
        createdIds.add(saved.getId());
        return saved;
    }

    @Test
    void save_insertsCustomerAndAssignsId() {
        Customer saved = saveNewCustomer();

        assertNotNull(saved.getId());
        assertTrue(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void findByEmail_isCaseInsensitive() {
        Customer saved = saveNewCustomer();

        assertTrue(repository.findByEmail(saved.getEmail().toUpperCase()).isPresent());
    }

    @Test
    void save_updatesExistingCustomer() {
        Customer saved = saveNewCustomer();
        saved.setFirstName("Beatriz");

        repository.save(saved);

        assertEquals("Beatriz", repository.findById(saved.getId()).orElseThrow().getFirstName());
    }

    @Test
    void save_throws_whenUpdatingNonExistingCustomer() {
        Customer ghost = new Customer(-1L, "Nobody", "Here", "ghost@email.com", null);

        assertThrows(NoSuchElementException.class, () -> repository.save(ghost));
    }

    @Test
    void deleteById_removesCustomer() {
        Customer saved = saveNewCustomer();

        repository.deleteById(saved.getId());

        assertTrue(repository.findById(saved.getId()).isEmpty());
    }
}
