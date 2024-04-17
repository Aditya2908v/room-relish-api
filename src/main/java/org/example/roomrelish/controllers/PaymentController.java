package org.example.roomrelish.controllers;

import org.example.roomrelish.models.Payment;
import org.example.roomrelish.services.PaymentServiceImpl;
import org.jetbrains.annotations.TestOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/v1/payment")
@TestOnly
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
