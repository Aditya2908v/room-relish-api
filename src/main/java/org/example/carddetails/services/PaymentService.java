package org.example.carddetails.services;

import org.example.carddetails.models.Booking;
import org.example.carddetails.models.Payment;

import java.util.List;

public interface PaymentService {
    Payment confirmBook(String _bookingId);

    List<Payment> getMyBookings(String _userId);
}
