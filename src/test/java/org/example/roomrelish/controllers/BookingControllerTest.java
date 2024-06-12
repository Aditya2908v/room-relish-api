package org.example.roomrelish.controllers;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.annotations.Fixture;
import com.mongodb.DuplicateKeyException;
import org.example.roomrelish.ExceptionHandler.CustomDataAccessException;
import org.example.roomrelish.ExceptionHandler.CustomDuplicateBookingException;
import org.example.roomrelish.ExceptionHandler.CustomMongoSocketException;
import org.example.roomrelish.ExceptionHandler.GlobalExceptionHandler;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.services.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BookingControllerTest {



    @Mock
    private BookingServiceImpl bookingServiceImpl;
    @Mock
    private GlobalExceptionHandler globalExceptionHandler;
    @Fixture
    BookingDetailsDTO bookingDetailsDTO;
    @Fixture
    Booking booking;
    @InjectMocks
    private BookingController bookingController;
    @BeforeEach
    public void setUp(){
        FixtureAnnotations.initFixtures(this);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookingDetails_success() throws CustomDuplicateBookingException, CustomDataAccessException, CustomMongoSocketException {
        //Arrange
       Booking booking1 = createBookingFixture(bookingDetailsDTO,booking);
        when(bookingServiceImpl.bookRoom(bookingDetailsDTO)).thenReturn(booking);
        //Act
        ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);
        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking,response.getBody());
        verifyBookingDetails(Objects.requireNonNull(booking1),bookingDetailsDTO);
    }

    private Booking createBookingFixture(BookingDetailsDTO bookingDetailsDTO, Booking booking) {
        booking.set_userId(bookingDetailsDTO.get_userId());
        booking.set_hotelId(bookingDetailsDTO.get_hotelId());
        booking.set_roomId(bookingDetailsDTO.get_roomId());
        booking.setNumOfRooms(bookingDetailsDTO.getCustomerRoomCount());
        booking.setNumOfDays(bookingDetailsDTO.getCustomerDayCount());
        booking.setCheckInDate(bookingDetailsDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDetailsDTO.getCheckOutDate());
        return booking;
    }

    private void verifyBookingDetails(Booking body, BookingDetailsDTO bookingDetailsDTO) {
        assertEquals(bookingDetailsDTO.get_userId(), body.get_userId());
        assertEquals(bookingDetailsDTO.get_hotelId(), body.get_hotelId());
        assertEquals(bookingDetailsDTO.get_roomId(), body.get_roomId());
        assertEquals(bookingDetailsDTO.getCustomerRoomCount(), body.getNumOfRooms());
        assertEquals(bookingDetailsDTO.getCustomerDayCount(), body.getNumOfDays());
        assertEquals(bookingDetailsDTO.getCheckInDate(), body.getCheckInDate());
        assertEquals(bookingDetailsDTO.getCheckOutDate(), body.getCheckOutDate());
    }

    @Test
    void testBookingDetails_NullPointerException() throws CustomDuplicateBookingException, CustomDataAccessException, CustomMongoSocketException {
        String errorMessage = "Null pointer exception occurred";
        when(bookingServiceImpl.bookRoom(null)).thenThrow(new NullPointerException("Null pointer exception occurred"));


        ResponseEntity<?> response = bookingController.bookingDetails(null);

        assertEquals(HttpStatusCode.valueOf(500),response.getStatusCode());
        assertEquals(errorMessage,response.getBody());
    }

    @Test
    void testBookingDetails_IllegalArgumentException()throws CustomDuplicateBookingException, CustomDataAccessException,CustomMongoSocketException{
        String errorMessage = "Hotel or Room not found";
        BookingDetailsDTO bookingDetailsDTO = createBookingDetailsDTO();
        when(bookingServiceImpl.bookRoom(bookingDetailsDTO)).thenThrow(new IllegalArgumentException("Hotel or Room not found"));

        ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);

        assertEquals(HttpStatusCode.valueOf(400),response.getStatusCode());
        assertEquals(errorMessage,response.getBody());
    }

    @Test
    void testBookingDetails_CustomDuplicateKeyException(){
        String errorMessage = "Duplicate booking error";
        BookingDetailsDTO bookingDetailsDTO =createBookingDetailsDTO();
        try{
            ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);
        }
        catch(DuplicateKeyException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }



    @Test
    void testBookingDetails_CustomDataAccessException() throws Exception {
        String errorMessage = "Duplicate booking error";
        BookingDetailsDTO bookingDetailsDTO =createBookingDetailsDTO();

        try{
            ResponseEntity<?> response = bookingController.bookingDetails(bookingDetailsDTO);
        }
        catch(DataAccessException e){
            assertEquals(errorMessage, e.getMessage());
        }
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
                .checkOutDate(createDate(29))
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
                .checkOutDate(createDate(29))
                .build();
    }

    private LocalDate createDate(int day){
        LocalDate currentDate = LocalDate.now();
        return LocalDate.of(currentDate.getYear(),currentDate.getMonth(),day);
    }







}
