package org.example.roomrelish.controllers;

import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.services.BookingService;
import org.example.roomrelish.services.BookingServiceImpl;
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
    private BookingServiceImpl bookingServiceImpl;

    @InjectMocks
    private BookingController bookingController;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

 @Test
    public void testBookingDetails_success(){
        Booking booking = new Booking();
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        when(bookingServiceImpl.bookRoom(bookingDetailsDTO)).thenReturn(booking);

        ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking,response.getBody());
    }

    @Test
    public void testBookingDetails_Exception(){
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        String errorMessage = "Hotel not found";
        doThrow(new IllegalArgumentException(errorMessage)).when(bookingServiceImpl).bookRoom(bookingDetailsDTO);

        ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(errorMessage,response.getBody());
    }

}
