package org.example.roomrelish.controllers;

import org.example.roomrelish.ExceptionHandler.CustomDataAccessException;
import org.example.roomrelish.ExceptionHandler.CustomDuplicateBookingException;
import org.example.roomrelish.ExceptionHandler.CustomMongoSocketException;
import org.example.roomrelish.models.Payment;
import org.example.roomrelish.services.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {
    @Mock
    private PaymentServiceImpl paymentServiceImpl;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConfirmBooking_Success() throws CustomDataAccessException, CustomDuplicateBookingException, CustomMongoSocketException {
        Payment payment = new Payment();
        String _bookingId = "345682456789987";
        when(paymentServiceImpl.confirmBook(_bookingId)).thenReturn(payment);

        ResponseEntity<?> response = paymentController.confirmBooking(_bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payment,response.getBody());
    }

    @Test
    void testConfirmBooking_Exception() throws CustomDataAccessException, CustomDuplicateBookingException, CustomMongoSocketException {
        String _bookingId = "23456789876";
        String errorMessage = "No booking details found";
        doThrow(new IllegalArgumentException(errorMessage)).when(paymentServiceImpl).confirmBook(_bookingId);

        ResponseEntity<?> response = paymentController.confirmBooking(_bookingId);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(errorMessage,response.getBody());
    }

  @Test
  void testMyBookings_success(){
        String _userId = "123456789";
        List<Payment> paymentList = new ArrayList<>();
        when(paymentServiceImpl.getMyBookings(_userId)).thenReturn(paymentList);

        ResponseEntity<?> response = paymentController.myBookings(_userId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(paymentList, response.getBody());
    }

    @Test
    void testMyBookings_Exception(){
        String _userId = "123456789";
        String errorMessage = "No bookings found";
        doThrow(new IllegalArgumentException(errorMessage)).when(paymentServiceImpl).getMyBookings(_userId);

        ResponseEntity<?> response = paymentController.myBookings(_userId);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(errorMessage,response.getBody());
    }


}
