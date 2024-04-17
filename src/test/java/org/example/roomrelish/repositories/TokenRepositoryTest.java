package org.example.roomrelish.repositories;

import org.example.roomrelish.repository.TokenRepository;
import org.example.roomrelish.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class TokenRepositoryTest {
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setUp() {

    }

}