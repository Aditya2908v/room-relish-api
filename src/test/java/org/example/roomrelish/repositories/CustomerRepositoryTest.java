package org.example.roomrelish.repositories;

import org.example.roomrelish.models.Customer;
import org.example.roomrelish.repository.CustomerRepository;
import org.example.roomrelish.services.CustomerService;
import org.example.roomrelish.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerRepositoryTest {
    @Mock
    private CustomerRepository repository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail(){
        String email = "test@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(customer));
        Optional<Customer> result = repository.findByEmail(email);
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
}