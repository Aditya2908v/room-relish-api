package org.example.roomrelish.controllers;

import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookingDetails(){
        Booking booking = new Booking();
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        when(bookingService.bookRoom(bookingDetailsDTO)).thenReturn(booking);

        ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking,response.getBody());
    }

}
