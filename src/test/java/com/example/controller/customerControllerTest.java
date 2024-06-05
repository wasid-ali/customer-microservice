package com.example.controller;

import com.example.model.Customer;
import com.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testAddCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setId(1L);

        doNothing().when(customerService).addCustomer(any(Customer.class));

        mockMvc.perform(post("/api/customers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/delete/{customerId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testListCustomers() throws Exception {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer());

        when(customerService.listCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testDeposit() throws Exception {
        when(customerService.deposit(eq(1L), anyDouble())).thenReturn(100.0);

        mockMvc.perform(post("/api/customers/deposit")
                .param("customerId", "1")
                .param("amount", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }

    @Test
    void testWithdraw() throws Exception {
        when(customerService.withdraw(eq(1L), anyDouble())).thenReturn(50.0);

        mockMvc.perform(post("/api/customers/withdraw")
                .param("customerId", "1")
                .param("amount", "50.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("50.0"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        doNothing().when(customerService).updateProfile(eq(1L), anyString(), anyString());

        mockMvc.perform(post("/api/customers/updateProfile")
                .param("customerId", "1")
                .param("fullName", "John Doe")
                .param("email", "john@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testCheckBalance() throws Exception {
        when(customerService.checkBalance(1L)).thenReturn(500.0);

        mockMvc.perform(get("/api/customers/checkBalance")
                .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.0"));
    }

    @Test
    void testChangePassword() throws Exception {
        doNothing().when(customerService).changePassword(eq(1L), anyString());

        mockMvc.perform(post("/api/customers/changePassword")
                .param("customerId", "1")
                .param("newPassword", "newpass123"))
                .andExpect(status().isOk());
    }
}
