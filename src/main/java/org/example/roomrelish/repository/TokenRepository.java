package org.example.roomrelish.repository;

import org.example.roomrelish.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    List<Token> findAllByCustomerIdAndExpiredIsFalseAndRevokedIsFalse(String customerId);
    Optional<Token> findByToken(String token);
}
