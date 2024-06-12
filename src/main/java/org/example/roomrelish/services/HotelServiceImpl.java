package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.roomrelish.dto.*;
import org.example.roomrelish.models.*;
import org.example.roomrelish.repository.CustomerRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.example.roomrelish.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
    String hotelErrorMessage = "Hotel not found";


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
            throw new IllegalArgumentException(hotelErrorMessage);
        }

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(hotelErrorMessage));

        setHotel(hotel, hotelDTO);

        return hotelRepository.save(hotel);
    }


    @Override
    public void deleteHotel(String id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException(hotelErrorMessage);
        hotelRepository.delete(hotel);
    }

    @Override
    public List<ReviewResponse> getReviews(String id) {
        Hotel hotel = hotelRepository.findById(id).orElse(null);
        if(hotel == null)
            throw new IllegalArgumentException(hotelErrorMessage);
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
                .orElseThrow(() -> new IllegalArgumentException(hotelErrorMessage));
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
                .orElseThrow(() -> new IllegalArgumentException(hotelErrorMessage));

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
            throw new IllegalArgumentException(hotelErrorMessage);
        Room room = new Room();
        room.setId(new ObjectId().toString());
        room.setRoomType(roomDTO.getRoomType());
        room.setRoomSpecification(roomDTO.getRoomSpecification());
        room.setRoomCountBasic(roomDTO.getRoomCount());
        room.setRoomRate(roomDTO.getRoomRate());
        List<Room> rooms = hotel.getRooms();
        rooms.add(room);
        hotelRepository.save(hotel);
        return room;
    }

    public SearchResultDTO findHotels(String cityName,
                                   LocalDate checkInDate,
                                   LocalDate checkOutDate,
                                   int countOfRooms,
                                   int priceRangeMax,
                                   int priceRangeMin,
                                   double rating,
                                   List<String> amenities) {
        try {
            List<Hotel> filteredHotels = hotelRepository.findByLocationCityName(cityName);
            filteredHotels = filteringHotelsByAmenities(filteredHotels,amenities);

        //filteredHotels = filteringHotelsByPriceRange(filteredHotels,priceRangeMax,priceRangeMin);

            filteredHotels = filteringHotelsByRating(filteredHotels,rating);

            return filteringHotelsByCheckInCheckOutDate(filteredHotels,checkInDate,checkOutDate,countOfRooms);


        } catch (Exception e) {
            throw new IllegalArgumentException("An error occurred while searching for hotels.", e);
        }
    }

   public SearchResultDTO filteringHotelsByCheckInCheckOutDate(List<Hotel> filteredHotels,LocalDate checkInDate,LocalDate checkOutDate,int countOfRooms) {

        List<String> availableRoomIds = new ArrayList<>();
        if((checkInDate!=null)&&(checkOutDate!=null)){
              findAvailability(checkInDate,checkOutDate,countOfRooms,availableRoomIds,filteredHotels);
        }
        LinkedHashSet<String> set = new LinkedHashSet<>(availableRoomIds);
        ArrayList<String> availableRoomIdsList = new ArrayList<>(set);
        SearchResultDTO searchResultDTO = new SearchResultDTO();
        searchResultDTO.setHotels(filteredHotels);
        searchResultDTO.setRoomIds(availableRoomIdsList);
       System.out.println(availableRoomIdsList.size());
        return searchResultDTO;
    }

    public void findAvailability(LocalDate userCheckInDate,LocalDate userCheckOutDate,int countOfRooms,List<String> availableRoomIds,List<Hotel> filteredHotels){
        availableRoomIds.addAll(
                filteredHotels.stream().flatMap(hotel -> hotel.getRooms().stream()).filter(room -> {
                            int initialRoomCount = room.getRoomCountBasic();
                            int roomCount;
                            if (room.getRoomAvailabilityList() != null) {
                                roomCount = room.getRoomAvailabilityList().stream()
                                        .reduce(initialRoomCount,
                                                (result, availability) -> findAvailabilityWithTheList(availability, userCheckInDate, userCheckOutDate, result),
                                                Integer::sum);
                            } else {
                                roomCount = initialRoomCount;
                            }
                            return room.getRoomAvailabilityList() == null || roomCount > countOfRooms;
                        }).map(Room::getId).toList()
        );
    }

    public int findAvailabilityWithTheList(RoomAvailability availability,LocalDate userCheckInDate,LocalDate userCheckOutDate,int roomCount) {
        if(((userCheckInDate.isBefore(availability.getCheckOutDate()))||(userCheckInDate.isEqual(availability.getCheckOutDate())))
                &&((userCheckOutDate.isAfter(availability.getCheckInDate()))||(userCheckInDate.isEqual(availability.getCheckInDate())))){
                roomCount=roomCount-availability.getRoomCount();
        }
        return roomCount;
    }


    private List<Hotel> filteringHotelsByRating(List<Hotel> filteredHotels, double rating) {
        if(rating>0){
            System.out.println("Inside rating filter");
            filteredHotels=filteredHotels.stream().filter(hotel -> hotel.getRating()>rating).collect(Collectors.toList());
        }
        System.out.println(filteredHotels.size());
        return filteredHotels;
    }

//    private List<Hotel> filteringHotelsByPriceRange(List<Hotel> filteredHotels, int priceRangeMax, int priceRangeMin) {
//        System.out.println("inside price range filter");
//        if((priceRangeMax!=0)&&(priceRangeMin!=0)){
//            System.out.println("price is not null"+priceRangeMax+" "+priceRangeMin);
//            Map<Hotel, List<Room>> hotelRoomMap = filteredHotels.stream()
//                    .flatMap(hotel -> hotel.getRooms().stream()
//                            .filter(room -> room.getRoomRate() <= priceRangeMax && room.getRoomRate() >= priceRangeMin)
//                            .map(room -> Map.entry(hotel, room)))
//                            .collect(Collectors.groupingBy(Map.Entry::getKey,
//                            Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
//            System.out.println(hotelRoomMap);
//            filteredHotels = new ArrayList<>(hotelRoomMap.keySet());
//        }
//        System.out.println(filteredHotels.size());
//        return filteredHotels;
//    }

    private List<Hotel> filteringHotelsByAmenities(List<Hotel> filteredHotels,List<String> amenities) {
        System.out.println("Inside amenities filter");
        if(amenities!=null){
            filteredHotels = filteredHotels.stream().filter(hotel-> new HashSet<>(hotel.getAmenities()).containsAll(amenities)).toList();
        }
        System.out.println(filteredHotels.size());
        return filteredHotels;
    }


    //util function
    public Hotel setHotel(Hotel hotel,HotelDTO hotelDTO){
        hotel.setHotelName(hotelDTO.getHotelName());
        hotel.setHotelType(hotelDTO.getHotelType());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setRating(hotelDTO.getRating());
        hotel.setOverallReview(hotelDTO.getOverallReview());
        hotel.setNumReviews(hotel.getNumReviews());
        hotel.setPriceStartingFrom(hotelDTO.getPriceStartingFrom());
        hotel.setOverview(hotelDTO.getOverview());
        hotel.setLocationFeatures(hotelDTO.getLocationFeatures());
        hotel.setAmenities(hotelDTO.getAmenities());
        hotel.setImages(hotelDTO.getImages());
        hotel.setRooms(hotelDTO.getRooms());
        hotel.setGuestReviews(hotelDTO.getGuestReviews());
        return hotel;
    }


}


