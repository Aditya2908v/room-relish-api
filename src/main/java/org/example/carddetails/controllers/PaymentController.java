package org.example.carddetails.controllers;

import org.example.carddetails.models.Booking;
import org.example.carddetails.models.Payment;
import org.example.carddetails.services.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }
    //Confirm booking of the room
    @PostMapping("/pay/{_bookingId}")
    public ResponseEntity<?> confirmBooking(@PathVariable String _bookingId){
        try{
            Payment paymentDetails = paymentService.confirmBook(_bookingId);
            return ResponseEntity.ok(paymentDetails);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body("No booking details found");
        }
    }

    @GetMapping("/myBookings")
    public ResponseEntity<?> myBookings(@RequestParam String _userId) {
        try {
            return ResponseEntity.ok(paymentService.getMyBookings(_userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("No bookings found");
        }
    }
}
