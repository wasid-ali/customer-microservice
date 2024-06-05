package com.example.service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);

        customerService.addCustomer(customer);

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testDeleteCustomer() {
        Long customerId = 1L;

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void testListCustomers() {
        when(customerRepository.findAll()).thenReturn(null);

        Iterable<Customer> result = customerService.listCustomers();

        assertNull(result);
    }

    @Test
    void testDeposit() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(100.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(null);

        double result = customerService.deposit(1L, 50.0);

        assertEquals(150.0, result);
    }

    @Test
    void testWithdrawSufficientBalance() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(100.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(null);

        double result = customerService.withdraw(1L, 50.0);

        assertEquals(50.0, result);
    }

    @Test
    void testWithdrawInsufficientBalance() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(30.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(RuntimeException.class, () -> customerService.withdraw(1L, 50.0));
    }

    @Test
    void testUpdateProfile() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(null);

        customerService.updateProfile(1L, "John Doe", "john@example.com");

        assertEquals("John Doe", customer.getFullName());
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    void testCheckBalance() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setBalance(100.0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        double result = customerService.checkBalance(1L);

        assertEquals(100.0, result);
    }

    @Test
    void testChangePassword() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(null);

        customerService.changePassword(1L, "newPassword");

        assertEquals("newPassword", customer.getPassword());
    }
}
