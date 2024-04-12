package org.example.carddetails.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.carddetails.dto.HotelDTO;
import org.example.carddetails.dto.ReviewDTO;
import org.example.carddetails.dto.RoomDTO;
import org.example.carddetails.models.GuestReview;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.models.Room;
import org.example.carddetails.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel findHotelById(String id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if(hotel.isPresent())
            return hotel.get();
        else
            throw new IllegalArgumentException("Invalid Hotel Id");
    }

    @Override
    public Hotel createHotel(HotelDTO hotelDTO) {
        if(hotelDTO == null)
            throw new IllegalArgumentException("Invalid hotel details");
        Hotel hotel = new Hotel();
        return hotelRepository.save(setHotel(hotel,hotelDTO));
    }


    @Override
    public void deleteReview(String hotelId, String reviewId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
        List<GuestReview> guestReviews = hotel.getGuestReviews();
        boolean removed = guestReviews.removeIf(review -> review.get_id().equals(reviewId));
        if (!removed) {
            throw new IllegalArgumentException("Review not found");
        }
        hotelRepository.save(hotel);
    }

    @Override
    public Hotel updateHotel(String id,HotelDTO hotelDTO) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotelDTO != null && hotel !=null){
            hotelRepository.save(setHotel(hotel, hotelDTO));
        }else{
            throw new IllegalArgumentException("Invalid hotel details");
        }
        return hotel;
    }

    @Override
    public List<GuestReview> getReviews(String id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException("Hotel not found");
        return hotel.getGuestReviews();
    }

    @Override
    public GuestReview addReview(String id, ReviewDTO reviewDTO) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
        GuestReview guestReview = new GuestReview();
        guestReview.setUser(reviewDTO.getUserid());
        guestReview.setGuestRating(reviewDTO.getRating());
        guestReview.setComment(reviewDTO.getComment());
        List<GuestReview> guestReviews = hotel.getGuestReviews();
        guestReviews.add(guestReview);
        hotelRepository.save(hotel);
        return guestReview;
    }

    @Override
    public void deleteHotel(String id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException("Hotel not found");
        hotelRepository.delete(hotel);
    }

    @Override
    public Room addRoom(String id, RoomDTO roomDTO) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException("Hotel not found");
        Room room = new Room();
        room.setId(new ObjectId().toString());
        room.setRoomType(roomDTO.getRoomType());
        room.setRoomSpecification(roomDTO.getRoomSpecification());
        room.setRoomCount(roomDTO.getRoomCount());
        room.setRoomRate(roomDTO.getRoomRate());
        List<Room> rooms = hotel.getRooms();
        rooms.add(room);
        hotelRepository.save(hotel);
        return room;
    }

    public List<Hotel> findHotels(String cityName, Integer rating) {
        try {
            return hotelRepository.findByLocationCityNameAndRatingGreaterThanEqual(cityName, rating);
        } catch (Exception e) {
            throw new IllegalArgumentException("An error occurred while searching for hotels.", e);
        }
    }

    //util function
    public Hotel setHotel(Hotel hotel,HotelDTO hotelDTO){
        hotel.setHotelName(hotelDTO.getHotelName());
        hotel.setHotelType(hotelDTO.getHotelType());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setPriceStartingFrom(hotelDTO.getPriceStartingFrom());
        hotel.setOverview(hotelDTO.getOverview());
        hotel.setLocationFeatures(hotelDTO.getLocationFeatures());
        hotel.setAmenities(hotelDTO.getAmenities());
        hotel.setImages(hotelDTO.getImages());
        hotel.setRooms(hotelDTO.getRooms());
        hotel.setTotalRooms(hotelDTO.getTotalRooms());
        return hotel;
    }

}

