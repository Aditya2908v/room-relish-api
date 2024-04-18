package org.example.roomrelish.services;

import org.example.roomrelish.dto.HotelDTO;
import org.example.roomrelish.dto.ReviewDTO;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Location;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HotelServiceImplTest {
    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllHotels() {
        // Arrange
        List<Hotel> expectedHotels = createSampleHotelList();
        when(hotelRepository.findAll()).thenReturn(expectedHotels);

        // Act
        List<Hotel> hotels = hotelService.getAllHotels();

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
        Hotel hotel = hotelService.findHotelById("1");

        // Assert
        Assertions.assertNotNull(hotel);
        Assertions.assertEquals(expectedHotel, hotel);
    }

    @Test
    void testFindHotelById_NonExistingId() {
        String nonExistingId = "nonExistingId";
        when(hotelRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> hotelService.findHotelById(nonExistingId));
        Assertions.assertEquals("Invalid Hotel Id", exception.getMessage());
    }

    @Test
    void testCreateHotel_WithValidDTO() {
        // Arrange
        HotelDTO validDTO = createSampleHotelDTO();
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.save(any())).thenReturn(expectedHotel);

        // Act
        Hotel createdHotel = hotelService.createHotel(validDTO);

        // Assert
        Assertions.assertNotNull(createdHotel);
        Assertions.assertEquals(expectedHotel.getId(), createdHotel.getId());
        Assertions.assertEquals(expectedHotel.getHotelName(), createdHotel.getHotelName());
    }

    @Test
    void testCreateHotel_WithNullDTO() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService.createHotel(null));
    }

    @Test
    void testUpdateHotel_WithValidDTO() {
        // Arrange
        HotelDTO validDTO = createSampleHotelDTO();
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.findById(any())).thenReturn(Optional.of(expectedHotel));
        when(hotelRepository.save(any())).thenReturn(expectedHotel);

        // Act
        Hotel updatedHotel = hotelService.updateHotel("1", validDTO);

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
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService.updateHotel(id, null));
        Assertions.assertEquals("Hotel not found", exception.getMessage());
        verify(hotelRepository, never()).findById(id);
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void testDeleteHotel_WithValidId(){
        String id="1";
        Hotel expectedHotel = createSampleHotel();
        when(hotelRepository.findById("1")).thenReturn(Optional.of(expectedHotel));
        hotelService.deleteHotel(id);
        verify(hotelRepository, times(1)).delete(expectedHotel);
    }

    @Test
    void testDeleteReview_WithValidId(){
        String id="1";
        String reviewId="reviewId";
        Hotel expectedHotel = createSampleHotel();
        expectedHotel.setGuestReviews(new ArrayList<>());
        when(hotelRepository.findById("1")).thenReturn(Optional.of(expectedHotel));
        hotelService.deleteReview(id, reviewId);
        verify(hotelRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteHotel_WithNullId(){
        String id="1";
        when(hotelRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService.deleteHotel(id));

    }

    @Test
    void testGetReviews(){
        //Arrange
        List<GuestReview> expectedReviews = List.of(
                new GuestReview("review", "review",4.5,"review" )
        );
        Hotel hotel =  createSampleHotel();
        hotel.setGuestReviews(expectedReviews);

        when(hotelRepository.findById("1")).thenReturn(Optional.of(hotel));
        List<GuestReview> reviews = hotelService.getReviews("1");
        Assertions.assertNotNull(reviews);

    }

    @Test
    void testGetReviews_WithNullId(){
        when(hotelRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> hotelService.getReviews("1"));
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
        GuestReview addedReview = hotelService.addReview(id, reviewDTO);

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
        hotel.setRooms(List.of(new Room()));
        hotel.setTotalRooms(15);
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
                .totalRooms(15)
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