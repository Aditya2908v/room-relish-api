package org.example.roomrelish.services;

import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Payment;

public interface PaymentService {
    Payment confirmBook(Booking booking);
}
