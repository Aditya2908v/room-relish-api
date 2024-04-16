package org.example.roomrelish.services;

import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;

public interface BookingService {
    Booking bookRoom(BookingDetailsDTO bookingDetailsDTO);

}
