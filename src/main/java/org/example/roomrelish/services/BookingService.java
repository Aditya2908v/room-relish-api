package org.example.roomrelish.services;

import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.jetbrains.annotations.TestOnly;

@TestOnly
public interface BookingService {
    Booking bookRoom(BookingDetailsDTO bookingDetailsDTO);

}
