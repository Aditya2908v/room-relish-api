package org.example.roomrelish.controllers;

import lombok.RequiredArgsConstructor;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.services.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
public class BookingController {

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
