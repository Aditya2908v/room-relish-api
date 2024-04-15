package org.example.carddetails.services;

import org.example.carddetails.dto.AuthResponse;
import org.example.carddetails.dto.LoginRequest;
import org.example.carddetails.dto.RegisterRequest;
import org.example.carddetails.models.Customer;
import org.example.carddetails.models.Role;
import org.example.carddetails.repository.CustomerRepository;
import org.example.carddetails.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterCustomer() {
        // Mock data
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setEmail("test@example.com");
        request.setPhoneNumber("1234567890");
        request.setAddress("123 Test St");
        request.setDateOfBirth(new Date());

        Customer savedCustomer = new Customer();
        savedCustomer.setId("1");
        savedCustomer.setUsername(request.getUsername());
        savedCustomer.setPassword(request.getPassword());
        savedCustomer.setEmail(request.getEmail());
        savedCustomer.setPhoneNumber(request.getPhoneNumber());
        savedCustomer.setAddress(request.getAddress());
        savedCustomer.setDateOfBirth(request.getDateOfBirth());
        savedCustomer.setRole(Role.USER);

        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(jwtService.generateToken(any())).thenReturn("Bearer token");

        // Perform registration
        AuthResponse response = authService.registerCustomer(request);

        // Verify
        assertEquals("Bearer token", response.getToken());
        verify(customerRepository, times(1)).save(any());
        verify(jwtService, times(1)).generateToken(any());
        verify(tokenRepository, times(1)).save(any());
    }

}
