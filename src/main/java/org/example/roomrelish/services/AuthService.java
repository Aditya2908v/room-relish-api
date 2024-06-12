package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.example.roomrelish.dto.AuthResponse;
import org.example.roomrelish.dto.LoginRequest;
import org.example.roomrelish.dto.RegisterRequest;
import org.example.roomrelish.dto.RegisterUserDTO;
import org.example.roomrelish.mapper.RegisterUserMapper;
import org.example.roomrelish.models.Customer;
import org.example.roomrelish.models.Role;
import org.example.roomrelish.models.Token;
import org.example.roomrelish.models.TokenType;
import org.example.roomrelish.repository.CustomerRepository;
import org.example.roomrelish.repository.TokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService  {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse registerCustomer(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());
        customer.setRegisteredAt(new Date());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setRole(Role.USER);

        var savedUser = customerRepository.save(customer);
        var jwtToken = jwtService.generateToken(savedUser);
        saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public RegisterRequest getRegisterRequest(RegisterUserDTO registerUserDTO){
        return RegisterUserMapper.INSTANCE.registerUserDTOToRegisterRequest(registerUserDTO);
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        var userOptional = customerRepository.findByEmail(loginRequest.getEmail());
        var user = userOptional.orElseThrow(() -> new NoSuchElementException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .build();
    }

    private void revokeAllUserTokens(Customer user) {
        var validUserTokens = tokenRepository.findAllByCustomerIdAndExpiredIsFalseAndRevokedIsFalse(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        for(Token token : validUserTokens){
            token.setExpired(true);
            token.setRevoked(true);
        }
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(Customer savedUser, String  jwtToken) {
        var token = Token.builder()
                .customerId(savedUser.getId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
