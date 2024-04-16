package org.example.roomrelish.services;

import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Location;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testBookRoom_HotelNotFound() {
        // Arrange
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.set_hotelId("nonExistingHotelId");

        when(hotelRepository.findById("nonExistingHotelId")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.bookRoom(bookingDetailsDTO));
        verify(hotelRepository, times(1)).findById("nonExistingHotelId");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    public void testBookRoom_RoomNotFound() {
        // Arrange
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.set_hotelId("hotel123");
        bookingDetailsDTO.set_roomId("nonExistingRoomId");

        Hotel hotel = new Hotel();
        hotel.setRooms(new ArrayList<>());
        when(hotelRepository.findById("hotel123")).thenReturn(Optional.of(hotel));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.bookRoom(bookingDetailsDTO));
        verify(hotelRepository, times(1)).findById("hotel123");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    public void testBookRoom_NoAvailableRooms() {
        // Arrange
        BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO();
        bookingDetailsDTO.set_hotelId("hotel123");
        bookingDetailsDTO.set_roomId("room123");
        bookingDetailsDTO.setCustomerRoomCount(10); // Assuming 10 rooms requested

        Hotel hotel = new Hotel();
        Room room = new Room();
        room.setId("room123");
        room.setRoomCount(5); // Assuming only 5 rooms available
        hotel.setRooms(List.of(room));
        when(hotelRepository.findById("hotel123")).thenReturn(Optional.of(hotel));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.bookRoom(bookingDetailsDTO));
        verify(hotelRepository, times(1)).findById("hotel123");
        verify(bookingRepository, never()).save(any());
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
}
