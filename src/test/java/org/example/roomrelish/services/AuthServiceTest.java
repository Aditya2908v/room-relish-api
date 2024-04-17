package org.example.roomrelish.services;

import org.example.roomrelish.dto.AuthResponse;
import org.example.roomrelish.dto.LoginRequest;
import org.example.roomrelish.dto.RegisterRequest;
import org.example.roomrelish.models.Customer;
import org.example.roomrelish.repository.CustomerRepository;
import org.example.roomrelish.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterCustomer_Success() {
        // Mock data
        RegisterRequest registerRequest = new RegisterRequest();
        // Set properties of registerRequest

        Customer savedUser = new Customer();
        // Set properties of savedUser

        String jwtToken = "mockJwtToken";

        // Stubbing behavior
        when(customerRepository.save(any())).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn(jwtToken);

        // Call the method
        AuthResponse result = authService.registerCustomer(registerRequest);

        // Verify the result
        assertEquals(jwtToken, result.getToken());
    }

    @Test
    void testAuthenticate_Success() {
        // Mock data
        LoginRequest loginRequest = new LoginRequest();
        // Set properties of loginRequest

        Customer user = new Customer();
        // Set properties of user

        String jwtToken = "mockJwtToken";

        // Stubbing behavior
        when(customerRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        // Call the method
        AuthResponse result = authService.authenticate(loginRequest);

        // Verify the result
        assertEquals(jwtToken, result.getToken());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Mock data
        LoginRequest loginRequest = new LoginRequest();
        // Set properties of loginRequest

        // Stubbing behavior to return an empty optional
        when(customerRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Call the method and verify that it throws NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> authService.authenticate(loginRequest));
    }
}