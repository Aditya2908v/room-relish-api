package org.example.carddetails.repository;

import org.example.carddetails.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    List<Token> findAllByCustomerIdAndExpiredIsFalseAndRevokedIsFalse(String customerId);
    Optional<Token> findByToken(String token);
}
