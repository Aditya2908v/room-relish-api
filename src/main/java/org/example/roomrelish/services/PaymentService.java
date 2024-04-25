package org.example.roomrelish.services;

import org.example.roomrelish.models.Payment;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
@TestOnly
public interface PaymentService {
    Payment confirmBook(String _bookingId);
    List<Payment> getMyBookings(String _userId);
    String deleteBooking(String _bookingId);

}
