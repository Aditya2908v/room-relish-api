package org.example.roomrelish.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.roomrelish.dto.HotelDTO;
import org.example.roomrelish.dto.ReviewDTO;
import org.example.roomrelish.dto.RoomDTO;
import org.example.roomrelish.dto.SearchDTO;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.HotelRepository;
import org.example.roomrelish.services.HotelService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    //TODO update search functionality based on checkIn, checkOut, price range and amenities


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
    public ResponseEntity<?> searchHotels(@RequestBody SearchDTO searchDTO){
       // System.out.println("Inside search end api");
        try {
            List<Hotel> hotels = hotelService.findHotels(searchDTO);
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

