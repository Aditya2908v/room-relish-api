package org.example.carddetails.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.carddetails.dto.BookingDetailsDTO;
import org.example.carddetails.models.Booking;
import org.example.carddetails.services.JwtService;
import org.springframework.security.access.AccessDeniedException;
import org.example.carddetails.services.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
public class BookingController {

    @Autowired
    private final BookingServiceImpl bookingService;

    @PostMapping("/bookingDetails")
    public ResponseEntity<?> bookingDetails(@RequestBody BookingDetailsDTO bookingDetailsDTO){
        try{
            Booking bookingDetails = bookingService.bookRoom(bookingDetailsDTO);
            return ResponseEntity.ok(bookingDetails);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(AccessDeniedException e){
            return ResponseEntity.status(403).body("Please login to continue");
        }
    }




}
