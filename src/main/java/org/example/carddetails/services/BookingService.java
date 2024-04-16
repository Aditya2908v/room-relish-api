package org.example.carddetails.services;

import org.example.carddetails.dto.BookingDetailsDTO;
import org.example.carddetails.models.Booking;

public interface BookingService {
    Booking bookRoom(BookingDetailsDTO bookingDetailsDTO);

}
