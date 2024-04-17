package org.example.roomrelish.repository;

import org.example.roomrelish.models.Customer;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
