package org.example.carddetails.services;

import org.example.carddetails.models.Booking;
import org.example.carddetails.models.Payment;

public interface PaymentService {
    Payment confirmBook(Booking booking);
}
