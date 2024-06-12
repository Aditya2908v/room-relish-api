package org.example.roomrelish.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.roomrelish.dto.AuthResponse;
import org.example.roomrelish.dto.LoginRequest;
import org.example.roomrelish.dto.RegisterRequest;
import org.example.roomrelish.dto.RegisterUserDTO;
import org.example.roomrelish.models.Customer;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.services.AuthService;
import org.example.roomrelish.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> expectedCustomers = new ArrayList<>();
        expectedCustomers.add(new Customer());
        expectedCustomers.add(new Customer());
        when(customerService.getAllCustomers()).thenReturn(expectedCustomers);
        ResponseEntity<?> response = customerController.getAllCustomers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCustomers, response.getBody());
    }

    @Test
    void registerCustomer_Success(){
        RegisterRequest request = new RegisterRequest();
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        when(authService.registerCustomer(request)).thenReturn(new AuthResponse());
        ResponseEntity<?> response = customerController.registerCustomer(registerUserDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void registerCustomer_Fail(){
        when(authService.registerCustomer(null)).thenThrow(new NoSuchElementException("User not found"));
        ResponseEntity<?> response = customerController.registerCustomer(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void loginCustomer_Success(){
        LoginRequest request = new LoginRequest();
        when(authService.authenticate(request)).thenReturn(new AuthResponse());
        ResponseEntity<?> response = customerController.loginUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loginCustomer_Fail(){
        when(authService.authenticate(null)).thenThrow(new NoSuchElementException("User not found"));
        ResponseEntity<?> response = customerController.loginUser(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetRecentHotels_Success() throws Exception {
        // Mock user email
        String userEmail = "test@example.com";
        when(httpServletRequest.getParameter("userEmail")).thenReturn(userEmail);

        // Mock recent hotels
        List<Hotel> recentHotels = new ArrayList<>();
        recentHotels.add(new Hotel());
        when(customerService.findRecentHotels(userEmail)).thenReturn(recentHotels);

        // Call the method
        ResponseEntity<?> response = customerController.getRecentHotels(httpServletRequest);

        // Verify response
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}