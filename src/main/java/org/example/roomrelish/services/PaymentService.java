package org.example.roomrelish.services;

import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Payment;

import java.util.List;

public interface PaymentService {
    Payment confirmBook(String _bookingId);
    List<Payment> getMyBookings(String _userId);

}
