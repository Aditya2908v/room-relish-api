package org.example.roomrelish.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.roomrelish.ExceptionHandler.CustomDataAccessException;
import org.example.roomrelish.ExceptionHandler.CustomDuplicateBookingException;
import org.example.roomrelish.ExceptionHandler.CustomMongoSocketException;
import org.example.roomrelish.ExceptionHandler.GlobalExceptionHandler;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.services.BookingServiceImpl;
import org.jetbrains.annotations.TestOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
@TestOnly
public class BookingController {
    GlobalExceptionHandler globalExceptionHandler;

    private final BookingServiceImpl bookingService;
    @Operation(
            description = "Booking Room",
            summary = "Adds a Booking document in DB with the given details",
            responses = {
                    @ApiResponse(
                            description = "Details of booking",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "No hotels or rooms found , No available rooms",
                            responseCode = "204"
                    )
            }
    )
    @PostMapping("/bookingDetails")
    public ResponseEntity<?> bookingDetails(@RequestBody BookingDetailsDTO bookingDetailsDTO){
        try{
            Booking bookingDetails = bookingService.bookRoom(bookingDetailsDTO);
            return ResponseEntity.ok(bookingDetails);
        }
        catch(NullPointerException e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (CustomDuplicateBookingException customDuplicateKeyException){
            return globalExceptionHandler.handleCustomDuplicateException(customDuplicateKeyException);
        }
        catch(CustomDataAccessException customDataAccessException){
            return globalExceptionHandler.handleCustomDataAccessException(customDataAccessException);
        }
        catch(CustomMongoSocketException customMongoSocketException){
            return globalExceptionHandler.handleMongoSocketException(customMongoSocketException);
        }
    }





}
