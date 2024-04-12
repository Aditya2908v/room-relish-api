package org.example.carddetails.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.carddetails.dto.HotelDTO;
import org.example.carddetails.dto.ReviewDTO;
import org.example.carddetails.dto.RoomDTO;
import org.example.carddetails.models.GuestReview;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.models.Room;
import org.example.carddetails.services.HotelService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hotels")
@Tag(name = "Hotel Management")
public class HotelController {

    private final HotelService hotelService;

    //GraphQL endpoints
    @QueryMapping("hotels")
    public List<Hotel> getAllHotels_GraphQL(){
        return hotelService.getAllHotels();
    }

    @QueryMapping("hotel")
    public Hotel getHotel_GraphQL(@Argument String id){
        return hotelService.findHotelById(id);
    }

    //search hotel
    @Operation(
            description = "Search Hotels",
            summary = "Search hotels by city name and/or rating",
            responses = {
                    @ApiResponse(
                            description = "List of hotels found",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "No hotels found",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500"
                    )
            }
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchHotels(
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) Integer rating
    ){
        try {
            List<Hotel> hotels = hotelService.findHotels(cityName, rating);
            if (hotels.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the search.");
        }
    }

    @PostMapping
    public ResponseEntity<?> creteHotel(@RequestBody HotelDTO hotelDTO){
        try{
            Hotel hotel = hotelService.createHotel(hotelDTO);
            return ResponseEntity.ok(hotel);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable String id,@RequestBody HotelDTO hotelDTO){
        try{
            Hotel hotel = hotelService.updateHotel(id,hotelDTO);
            return ResponseEntity.ok(hotel);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable String id){
        try{
            hotelService.deleteHotel(id);
            return ResponseEntity.ok("Hotel deleted successfully");
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    //Get reviews
    @GetMapping("{id}/reviews")
    public ResponseEntity<?> getAllReviews(@PathVariable String id){
        try{
            List<GuestReview> guestReviews = hotelService.getReviews(id);
            return ResponseEntity.ok(guestReviews);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    //add review
    @PostMapping("{id}/reviews")
    public ResponseEntity<?> addReview(@PathVariable String id, @RequestBody ReviewDTO reviewDTO) {
        try {
            GuestReview guestReview = hotelService.addReview(id, reviewDTO);
            return ResponseEntity.ok(guestReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //delete review
    @DeleteMapping("{hotelId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable String hotelId, @PathVariable String reviewId) {
        try {
            hotelService.deleteReview(hotelId, reviewId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //add room
    @PostMapping("{id}/rooms")
    public ResponseEntity<?> addRoom(@PathVariable String id,@RequestBody RoomDTO roomDTO){
        try{
            Room room = hotelService.addRoom(id, roomDTO);
            return ResponseEntity.ok(room);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}

