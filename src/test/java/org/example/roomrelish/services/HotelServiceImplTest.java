package org.example.roomrelish.services;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.annotations.Fixture;
import net.bytebuddy.asm.Advice;
import org.example.roomrelish.dto.HotelDTO;
import org.example.roomrelish.dto.ReviewDTO;
import org.example.roomrelish.dto.SearchResultDTO;
import org.example.roomrelish.models.*;
import org.example.roomrelish.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HotelServiceImplTest {
    @Fixture
    List<Hotel> filteredHotels;
    @Fixture
    Hotel hotel1;
    @Fixture
    Hotel hotel2;
    @Fixture
    Room room1;


    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService1;

    @BeforeEach
    public void setUp() {
        FixtureAnnotations.initFixtures(this);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAvailabilityWithList_noOverlap() {
        RoomAvailability roomAvailability = new RoomAvailability("123", LocalDate.of(2024, 6, 8), LocalDate.of(2024, 6, 11), 2);
        LocalDate checkInDate = LocalDate.of(2024, 6, 12); // After availability
        LocalDate checkOutDate = LocalDate.of(2024, 6, 15); // After availability
        int roomCount = 3;

        int actualRoomCount = hotelService.findAvailabilityWithTheList(roomAvailability, checkInDate, checkOutDate, roomCount);
        Assertions.assertEquals(roomCount, actualRoomCount, "Available room count should not change for no overlap");
    }

    @Test
    public void testFindAvailabilityWithList_fullOverlap() {
        RoomAvailability roomAvailability = new RoomAvailability("123", LocalDate.of(2024, 6, 8), LocalDate.of(2024, 6, 11), 2);
        LocalDate checkInDate = LocalDate.of(2024, 6, 9); // Within availability
        LocalDate checkOutDate = LocalDate.of(2024, 6, 10); // Within availability
        int roomCount = 3;

        int actualRoomCount = hotelService.findAvailabilityWithTheList(roomAvailability, checkInDate, checkOutDate, roomCount);
        Assertions.assertEquals(roomCount - roomAvailability.getRoomCount(), actualRoomCount, "Available room count should be reduced for full overlap");
    }

    @Test
    public void testFindAvailabilityWithList_partialOverlapCheckIn() {
        RoomAvailability roomAvailability = new RoomAvailability("123", LocalDate.of(2024, 6, 8), LocalDate.of(2024, 6, 11), 2);
        LocalDate checkInDate = LocalDate.of(2024, 6, 8); // On availability start date
        LocalDate checkOutDate = LocalDate.of(2024, 6, 10); // Within availability
        int roomCount = 3;

        int actualRoomCount = hotelService.findAvailabilityWithTheList(roomAvailability, checkInDate, checkOutDate, roomCount);
        Assertions.assertEquals(roomCount - roomAvailability.getRoomCount(), actualRoomCount, "Available room count should be reduced for partial overlap (check-in on start date)");
    }

    @Test
    public void testFindAvailabilityWithList_partialOverlapCheckOut() {
        RoomAvailability roomAvailability = new RoomAvailability("123", LocalDate.of(2024, 6, 8), LocalDate.of(2024, 6, 11), 2);
        LocalDate checkInDate = LocalDate.of(2024, 6, 9); // Within availability
        LocalDate checkOutDate = LocalDate.of(2024, 6, 11); // On availability end date
        int roomCount = 3;

        int actualRoomCount = hotelService.findAvailabilityWithTheList(roomAvailability, checkInDate, checkOutDate, roomCount);
        Assertions.assertEquals(roomCount - roomAvailability.getRoomCount(), actualRoomCount, "Available room count should be reduced for partial overlap (check-out on end date)");
    }

    @Test
    public void testFilteringHotelsByCheckInCheckOutDate(){
        LocalDate checkInDate = LocalDate.of(2024,6,10);
        LocalDate checkOutDate = LocalDate.of(2024,6,12);
        int countOfRooms = 2;

        SearchResultDTO searchResultDTO = hotelService.filteringHotelsByCheckInCheckOutDate(filteredHotels,checkInDate,checkOutDate,countOfRooms);

        Assertions.assertNotNull(searchResultDTO);
    }

    @Test
    public void testFindAvailability(){
        LocalDate userCheckInDate = LocalDate.now().plusDays(3);
        LocalDate userCheckOutDate = LocalDate.now().plusDays(5);
        int countOfRooms = 4;
        List<String> availableRoomIds = new ArrayList<>();
        List<Hotel> listOfHotels = createSampleHotelList();
        String roomId = listOfHotels.getFirst().getRooms().getFirst().getId();
        hotelService.findAvailability(userCheckInDate,userCheckOutDate,countOfRooms,availableRoomIds,listOfHotels);

        Assertions.assertEquals(roomId,availableRoomIds.getFirst());
    }
    @Test
    void testGetAllHotels() {
        // Arrange
        List<Hotel> expectedHotels = createSampleHotelList();
        when(hotelRepository.findAll()).thenReturn(expectedHotels);

        // Act
        List<Hotel> hotels = hotelService1.getAllHotels();

        // Assert
        Assertions.assertEquals(expectedHotels.size(), hotels.size());
        for (int i = 0; i < hotels.size(); i++) {
            Assertions.assertEquals(expectedHotels.get(i), hotels.get(i));
        }
    }

    @Test
    void testGetHotelById() {
        // Arrange
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.findById("1")).thenReturn(Optional.of(expectedHotel));

        // Act
        Hotel hotel = hotelService1.findHotelById("1");

        // Assert
        Assertions.assertNotNull(hotel);
        Assertions.assertEquals(expectedHotel, hotel);
    }

    @Test
    void testFindHotelById_NonExistingId() {
        String nonExistingId = "nonExistingId";
        when(hotelRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> hotelService1.findHotelById(nonExistingId));
        Assertions.assertEquals("Invalid Hotel Id", exception.getMessage());
    }

    @Test
    void testCreateHotel_WithValidDTO() {
        // Arrange
        HotelDTO validDTO = createSampleHotelDTO();
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.save(any())).thenReturn(expectedHotel);

        // Act
        Hotel createdHotel = hotelService1.createHotel(validDTO);

        // Assert
        Assertions.assertNotNull(createdHotel);
        Assertions.assertEquals(expectedHotel.getId(), createdHotel.getId());
        Assertions.assertEquals(expectedHotel.getHotelName(), createdHotel.getHotelName());
    }

    @Test
    void testCreateHotel_WithNullDTO() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService1.createHotel(null));
    }

    @Test
    void testUpdateHotel_WithValidDTO() {
        // Arrange
        HotelDTO validDTO = createSampleHotelDTO();
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.findById(any())).thenReturn(Optional.of(expectedHotel));
        when(hotelRepository.save(any())).thenReturn(expectedHotel);

        // Act
        Hotel updatedHotel = hotelService1.updateHotel("1", validDTO);

        // Assert
        Assertions.assertNotNull(updatedHotel);
        Assertions.assertEquals(expectedHotel.getId(), updatedHotel.getId());
        Assertions.assertEquals(expectedHotel.getHotelName(), updatedHotel.getHotelName());
    }

    @Test
    void testUpdateHotel_WithNullDTO() {
        //Arrange
        String id="1";
        //Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService1.updateHotel(id, null));
        Assertions.assertEquals("Hotel not found", exception.getMessage());
        verify(hotelRepository, never()).findById(id);
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void testDeleteHotel_WithValidId(){
        String id="1";
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.findById("1")).thenReturn(Optional.of(expectedHotel));
        hotelService1.deleteHotel(id);
        verify(hotelRepository, times(1)).delete(expectedHotel);
    }

    @Test
    void testDeleteReview_WithValidId(){
        String id="1";
        String reviewId="reviewId";
        Hotel expectedHotel = createSampleHotel();
        expectedHotel.setGuestReviews(new ArrayList<>());
        when(hotelRepository.findById("1")).thenReturn(Optional.of(expectedHotel));
        hotelService1.deleteReview(id, reviewId);
        verify(hotelRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteHotel_WithNullId(){
        String id="1";
        when(hotelRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService1.deleteHotel(id));

    }

    @Test
    void testGetReviews(){
        //Arrange
        List<GuestReview> expectedReviews = List.of(
                new GuestReview("1", "1",4.5,"review" )
        );
        Hotel hotel =  createSampleHotel();
        hotel.setGuestReviews(expectedReviews);

        when(hotelRepository.findById("1")).thenReturn(Optional.of(hotel));

        Optional<Hotel> optionalHotel = hotelRepository.findById("1");
        Hotel hotel1 = optionalHotel.get();

        Assertions.assertEquals(hotel,hotel1);


    }

    @Test
    void testGetReviews_WithNullId(){
        when(hotelRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService1.getReviews("1"));
    }

    @Test
    void testAddReview_WithValidId(){
        //Arrange
        String id="1";
        ReviewDTO reviewDTO = new ReviewDTO();
        Hotel hotel =  createSampleHotel();
        hotel.setId(id);
        hotel.setGuestReviews(new ArrayList<>());
        when(hotelRepository.findById("1")).thenReturn(Optional.of(hotel));

        //Act
        GuestReview addedReview = hotelService1.addReview(id, reviewDTO);

        //Assert
        Assertions.assertNotNull(addedReview);
    }



    // Helper method to create a sample Hotel object
    private Hotel createSampleHotel() {
        Hotel hotel = new Hotel();
        hotel.setId("1");
        hotel.setHotelName("Sample Hotel");
        hotel.setHotelType("Luxury");
        hotel.setLocation(new Location());
        hotel.setPriceStartingFrom(200);
        hotel.setOverview("This is a sample hotel");
        hotel.setLocationFeatures(List.of("Nearby attractions", "City center location"));
        hotel.setAmenities(List.of("Free WiFi", "Swimming pool"));
        hotel.setImages(List.of("image1.jpg", "image2.jpg"));
        hotel.setRooms(List.of(room1));
        return hotel;
    }

    // Helper method to create a sample HotelDTO object
    private HotelDTO createSampleHotelDTO() {
        return HotelDTO.builder()
                .hotelName("Sample Hotel")
                .hotelType("Luxury")
                .location(new Location())
                .priceStartingFrom(200)
                .overview("This is a sample hotel")
                .locationFeatures(List.of("Nearby attractions", "City center location"))
                .amenities(List.of("Free WiFi", "Swimming pool"))
                .images(List.of("image1.jpg", "image2.jpg"))
                .rooms(List.of(new Room()))
                .build();
    }

    // Helper method to create a list of sample Hotel objects
    private List<Hotel> createSampleHotelList() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(createSampleHotel());
        hotels.add(createSampleHotel());
        return hotels;
    }
}