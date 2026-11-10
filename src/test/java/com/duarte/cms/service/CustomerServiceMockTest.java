package com.duarte.cms.service;

import com.duarte.cms.model.Customer;
import com.duarte.cms.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceMockTest {

    @Mock
    private CustomerRepository repository;

    @Test
    void createCustomer_savesCustomer_whenEmailIsFree() {
        CustomerService service = new CustomerService(repository);
        when(repository.findByEmail("ana@email.com")).thenReturn(Optional.empty());
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        service.createCustomer("Ana", "Silva", "ana@email.com", "912345678");

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(repository).save(captor.capture());
        assertEquals("Ana", captor.getValue().getFirstName());
        assertEquals("ana@email.com", captor.getValue().getEmail());
    }

    @Test
    void createCustomer_neverSaves_whenEmailAlreadyExists() {
        CustomerService service = new CustomerService(repository);
        Customer existing = new Customer(1L, "Ana", "Silva", "ana@email.com", null);
        when(repository.findByEmail("ana@email.com")).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> service.createCustomer("Outra", "Pessoa", "ana@email.com", null));

        verify(repository, never()).save(any());
    }

    @Test
    void getCustomerById_throws_whenNotFound() {
        CustomerService service = new CustomerService(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getCustomerById(99L));
    }

    @Test
    void updateCustomer_neverSaves_whenEmailBelongsToAnotherCustomer() {
        CustomerService service = new CustomerService(repository);
        Customer ana = new Customer(1L, "Ana", "Silva", "ana@email.com", null);
        Customer rui = new Customer(2L, "Rui", "Costa", "rui@email.com", null);
        when(repository.findById(1L)).thenReturn(Optional.of(ana));
        when(repository.findByEmail("rui@email.com")).thenReturn(Optional.of(rui));

        assertThrows(IllegalArgumentException.class,
                () -> service.updateCustomer(1L, "Ana", "Silva", "rui@email.com", null));

        verify(repository, never()).save(any());
        assertEquals("ana@email.com", ana.getEmail());
    }

    @Test
    void deleteCustomer_callsRepository_whenCustomerExists() {
        CustomerService service = new CustomerService(repository);
        Customer ana = new Customer(1L, "Ana", "Silva", "ana@email.com", null);
        when(repository.findById(1L)).thenReturn(Optional.of(ana));

        service.deleteCustomer(1L);

        verify(repository).deleteById(1L);
    }
}
