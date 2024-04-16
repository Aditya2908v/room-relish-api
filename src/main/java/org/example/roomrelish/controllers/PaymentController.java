package org.example.roomrelish.controllers;

import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Payment;
import org.example.roomrelish.services.PaymentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }
    //Confirm booking of the room
    @PostMapping("/pay")
    public ResponseEntity<?> confirmBooking(@RequestBody Booking booking){
        try{
            Payment paymentDetails = paymentService.confirmBook(booking);
            return ResponseEntity.ok(paymentDetails);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body("No booking details found");
        }
    }

}
