package org.example.roomrelish.repository;

import org.example.roomrelish.models.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {
}