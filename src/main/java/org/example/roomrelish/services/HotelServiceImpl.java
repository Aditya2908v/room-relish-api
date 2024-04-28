package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.example.roomrelish.dto.*;
import org.example.roomrelish.models.Customer;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.CustomerRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;

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
    public List<ReviewResponse> getReviews(String id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException("Hotel not found");
        List<GuestReview> guestReviews = hotel.getGuestReviews();
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (GuestReview guestReview : guestReviews) {
            String customerId = guestReview.getUser();
            Customer customer = customerRepository.findById(customerId).orElse(null);
            reviewResponses.add(ReviewResponse.builder()
                    .user(customer)
                    .guestRating(guestReview.getGuestRating())
                    .comment(guestReview.getComment())
                    .build());
        }
        return reviewResponses;
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

    public List<Hotel> findHotels(String cityName,
                                   Date checkInDate,
                                   Date checkOutDate,
                                   int countOfRooms,
                                   int priceRangeMax,
                                   int priceRangeMin,
                                   double rating,
                                   List<String> amenities) {
        try {
            List<Hotel> filteredHotels = hotelRepository.findByLocationCityName(cityName);
            filteredHotels = filteredHotels.stream().filter(hotel -> hotel.getTotalRooms()>countOfRooms).collect(Collectors.toList());
           //System.out.println(rating+" "+cityName+" "+priceRangeMax+" "+ priceRangeMin+" "+ countOfRooms);
            if(amenities!=null){
             //   System.out.println("Inside amenities filter");
                filteredHotels = filteredHotels.stream().filter(hotel-> new HashSet<>(hotel.getAmenities()).containsAll(amenities)).collect(Collectors.toList());
            }
            if((priceRangeMax!=0)&&(priceRangeMin!=0)){
               // System.out.println("Inside price range filter");
                Map<Hotel, List<Room>> hotelRoomMap = filteredHotels.stream()
                        .flatMap(hotel -> hotel.getRooms().stream()
                                .filter(room -> room.getRoomRate() <= priceRangeMax && room.getRoomRate() >= priceRangeMin)
                                .map(room -> Map.entry(hotel, room)))
                        .collect(Collectors.groupingBy(Map.Entry::getKey,
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
                filteredHotels = new ArrayList<>(hotelRoomMap.keySet());

            }
            if(rating>0){
                //System.out.println("Inside rating filter");
                filteredHotels=filteredHotels.stream().filter(hotel -> hotel.getRating()>rating).collect(Collectors.toList());
            }
            return filteredHotels;
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
        int totalRooms=0;
        for(Room room:hotelDTO.getRooms()){
            totalRooms+=room.getRoomCount();
        }
        hotel.setTotalRooms(totalRooms);
        return hotel;
    }


}


