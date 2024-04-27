package org.example.roomrelish.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

        @Operation(description = "Confirm Payment process", summary = "After booking, this payment API confirms the booking by making a payment", responses = {
                        @ApiResponse(description = "Details of Payment", responseCode = "200"),
                        @ApiResponse(description = "No booking details found", responseCode = "204")
        })

        @PostMapping("/pay")
        public ResponseEntity<?> confirmBooking(@RequestParam String _bookingId) {
                try {
                        Payment paymentDetails = paymentService.confirmBook(_bookingId);
                        return ResponseEntity.ok(paymentDetails);
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("No booking details found");
                }
        }

        @Operation(description = "Bookings of an user", summary = "This API returns the bookings that are drafted without payment", responses = {
                        @ApiResponse(description = "List of details of booking", responseCode = "200"),
                        @ApiResponse(description = "No bookings found", responseCode = "204")
        })
        @GetMapping("/myBookings")
        public ResponseEntity<?> myBookings(@RequestParam String _userId) {
                try {
                        return ResponseEntity.ok(paymentService.getMyBookings(_userId));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().body("No bookings found");
                }
        }
        @Operation(description = "Delete Booking", summary = "This API deletes the booking details of the room or cancels the booked room", responses = {
                @ApiResponse(description = "Deleted successful message", responseCode = "200"),
                @ApiResponse(description = "No bookings found", responseCode = "204")
        })
        @DeleteMapping("/deleteMyBooking")
        public ResponseEntity<?> deleteMyBooking(@RequestParam String _bookingId)
        {
                try{
                        return ResponseEntity.ok(paymentService.deleteBooking(_bookingId));
                }
                catch (IllegalArgumentException ae){
                        return ResponseEntity.badRequest().body(ae.getMessage());
                }
        }


}
