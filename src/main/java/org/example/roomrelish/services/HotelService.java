package org.example.roomrelish.services;



import org.example.roomrelish.dto.HotelDTO;
import org.example.roomrelish.dto.ReviewDTO;
import org.example.roomrelish.dto.RoomDTO;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Room;
import org.reactivestreams.Publisher;


import java.util.List;

public interface HotelService {
    List<Hotel> getAllHotels();
    Hotel findHotelById(String id);
    Hotel createHotel(HotelDTO hotelDTO);
    void deleteReview(String hotelId, String reviewId);
    Hotel updateHotel(String id,HotelDTO hotelDTO);
    List<GuestReview> getReviews(String id);
    GuestReview addReview(String id, ReviewDTO reviewDTO);
    void deleteHotel(String id);
    Room addRoom(String id, RoomDTO roomDTO);

    List<Hotel> findHotels(String cityName, Date checkInDate, Date checkOutdate, Integer countOfRooms, Integer priceRangeMax, Integer priceRangeMin, Integer rating, List<String> amenitiesRequired);
}