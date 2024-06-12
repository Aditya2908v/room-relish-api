package org.example.roomrelish.services;

import org.example.roomrelish.ExceptionHandler.CustomDataAccessException;
import org.example.roomrelish.ExceptionHandler.CustomDuplicateBookingException;
import org.example.roomrelish.ExceptionHandler.CustomMongoSocketException;
import org.example.roomrelish.models.Payment;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
@TestOnly
public interface PaymentService {
    Payment confirmBook(String _bookingId) throws CustomDuplicateBookingException, CustomDataAccessException, CustomMongoSocketException;
    List<Payment> getMyBookings(String _userId);
    String deleteBooking(String _bookingId);

}
