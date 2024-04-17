package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.roomrelish.dto.HotelDTO;
import org.example.roomrelish.dto.ReviewDTO;
import org.example.roomrelish.dto.RoomDTO;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Location;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.HotelRepository;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

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
    public Hotel updateHotel(String id, HotelDTO hotelDTO) {
        if (hotelDTO == null) {
            throw new IllegalArgumentException("Hotel not found");
        }

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        setHotel(hotel, hotelDTO);

        return hotelRepository.save(hotel);
    }


    @Override
    public void deleteHotel(String id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException("Hotel not found");
        hotelRepository.delete(hotel);
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
    public void deleteReview(String hotelId, String reviewId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        List<GuestReview> guestReviews = hotel.getGuestReviews();
        Optional<GuestReview> reviewToRemove = guestReviews.stream()
                .filter(review -> review.get_id().equals(reviewId))
                .findFirst();

        if (reviewToRemove.isPresent()) {
            guestReviews.remove(reviewToRemove.get());
            hotelRepository.save(hotel);
        } else {
            // Review not found, do nothing or log a message
        }
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
            List<Hotel> hotels = hotelRepository.findByLocationCityNameAndRatingGreaterThanEqual(cityName, rating);
            if (hotels == null) {
                throw new IllegalArgumentException("Invalid City Name");
            }
            return hotels;
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

