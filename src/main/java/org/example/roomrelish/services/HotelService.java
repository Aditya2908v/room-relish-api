package org.example.roomrelish.services;



import org.example.roomrelish.dto.*;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Room;
import org.jetbrains.annotations.TestOnly;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@TestOnly
public interface HotelService {
    List<Hotel> getAllHotels();
    Hotel findHotelById(String id);
    Hotel createHotel(HotelDTO hotelDTO);
    void deleteReview(String hotelId, String reviewId);
    Hotel updateHotel(String id,HotelDTO hotelDTO);
    List<ReviewResponse> getReviews(String id);
    GuestReview addReview(String id, ReviewDTO reviewDTO);
    void deleteHotel(String id);
    Room addRoom(String id, RoomDTO roomDTO);

    SearchResultDTO findHotels(String cityName,
                           LocalDate checkInDate,
                           LocalDate checkOutDate,
                           int countOfRooms,
                           int priceRangeMax,
                           int priceRangeMin,
                           double rating,
                           List<String> amenities);

}
