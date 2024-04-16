package org.example.carddetails.services;



import org.example.carddetails.dto.HotelDTO;
import org.example.carddetails.dto.ReviewDTO;
import org.example.carddetails.dto.RoomDTO;
import org.example.carddetails.dto.SearchCriteria;
import org.example.carddetails.models.GuestReview;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.models.Room;


import java.util.Date;
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
