package org.example.roomrelish.services;

import org.example.roomrelish.ExceptionHandler.CustomDataAccessException;
import org.example.roomrelish.ExceptionHandler.CustomDuplicateBookingException;
import org.example.roomrelish.ExceptionHandler.CustomMongoSocketException;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.*;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BookingServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BookingDetailsDTO bookingDetailsDTOMock;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingServiceImpl bookingServiceImpl;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void validateRoomAvailability_success(){
        //Arrange
        int roomCount = 7;
        int customerRoomCount = 2;
        //Act
        bookingService.validateRoomAvailability(roomCount,customerRoomCount);
    }

    @Test
    public void validateRoomAvailability_IllegalArgException(){
        int roomCount = 1;
        int customerRoomCount = 3;
        String errorMessage = "No available rooms";

        try{
            bookingService.validateRoomAvailability(roomCount,customerRoomCount);
            fail("Expected an exception but none thrown");
        }
        catch(IllegalArgumentException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void createBooking_success(){
        BookingDetailsDTO bookingDetailsDTO = createBookingDetailsDTO();
        Room room = createRoom();
        Booking booking = createBooking();
        when(bookingServiceImpl.createBooking(bookingDetailsDTO,room)).thenReturn(booking);

        Booking booking1 = bookingServiceImpl.createBooking(bookingDetailsDTO,room);

        verifyBooking(booking,booking1);
    }

    private void verifyBooking(Booking booking, Booking booking1) {
        assertEquals(booking.get_hotelId(),booking1.get_hotelId());
        assertEquals(booking.get_userId(),booking1.get_userId());
        assertEquals(booking.get_hotelId(),booking1.get_hotelId());
    }

    @Test
    public void createPayment_success(){
        Booking booking = createBooking();
        Room room = createRoom();
        Hotel hotel = createSampleHotel();
        Payment payment = createPayment();
        when(bookingServiceImpl.createPayment(booking,room,hotel)).thenReturn(payment);

        Payment payment1 = bookingService.createPayment(booking,room,hotel);

        verifyPayment(payment,payment1);
    }

    private void verifyPayment(Payment payment,Payment payment1){
        assertEquals(payment.get_userId(),payment1.get_userId());
        assertEquals(payment.get_hotelId(),payment1.get_hotelId());
        assertEquals(payment.get_roomId(),payment1.get_roomId());
        assertFalse(payment1.isPaymentStatus());
    }

    @Test
    public void bookRoom_NullPointException() throws CustomDataAccessException, CustomDuplicateBookingException, CustomMongoSocketException {
        String errorMessage = "No details provided";
        when(bookingServiceImpl.bookRoom(null)).thenThrow(new NullPointerException("No details provided"));

        try{
            bookingService.bookRoom(null);
            fail("Expected an exception but none thrown");
        }
        catch(NullPointerException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void bookRoom_HotelIllegalArgException() throws CustomDataAccessException, CustomDuplicateBookingException, CustomMongoSocketException {
        String errorMessage = "Hotel Not Found";
        BookingDetailsDTO bookingDetailsDTO = createBookingDetailsDTO();
        when(bookingServiceImpl.bookRoom(bookingDetailsDTO)).thenThrow(new IllegalArgumentException("Hotel Not Found"));

        try{
            bookingService.bookRoom(bookingDetailsDTO);
            fail("Expected an exception but none thrown");
        }
        catch(IllegalArgumentException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }




    private Payment createPayment(){
        return Payment.builder()
                .id("123")
                ._bookingId("987")
                ._hotelId("234")
                ._roomId("345")
                ._userId("123")
                .numOfRooms(1)
                .numOfDays(1)
                .totalAmount(1100.0)
                .checkInDate(createDate(27))
                .checkOutDate(createDate(28))
                .paymentStatus(false)
                .build();
    }

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
        return hotel;
    }

    private Room createRoom() {
        return Room.builder()
                .id("123")
                .roomType("Deluxe")
                .roomSpecification("King size")
                .roomRate(1200)
                .roomCountBasic(3).build();
    }

    private Booking createBooking() {
        return Booking.builder()
                .id("987")
                ._userId("123")
                ._hotelId("234")
                ._roomId("345")
                .numOfRooms(1)
                .numOfDays(1)
                .totalAmount(1100.0)
                .gstOfTotalAmount(200.0)
                .checkInDate(createDate(27))
                .checkOutDate(createDate(28))
                .build();
    }
    private BookingDetailsDTO createBookingDetailsDTO() {
        return BookingDetailsDTO.builder()
                ._userId("123")
                ._hotelId("234")
                ._roomId("345")
                .customerRoomCount(1)
                .customerDayCount(1)
                .checkInDate(createDate(27))
                .checkOutDate(createDate(28))
                .build();
    }


    private LocalDate createDate(int day){
        LocalDate currentDate = LocalDate.now();
        return LocalDate.of(currentDate.getYear(),currentDate.getMonth(),day);
    }

}