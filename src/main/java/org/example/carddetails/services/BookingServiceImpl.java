package org.example.carddetails.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.carddetails.dto.BookingDetailsDTO;
import org.example.carddetails.models.*;
import org.example.carddetails.repository.BookingRepository;
import org.example.carddetails.repository.CustomerRepository;
import org.example.carddetails.repository.HotelRepository;
import org.example.carddetails.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;
@Service

public class BookingServiceImpl implements BookingService{
    @Autowired
    private final HotelRepository hotelRepository;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    public BookingServiceImpl(HotelRepository hotelRepository, PaymentRepository paymentRepository, BookingRepository bookingRepository)
    {
        this.hotelRepository=hotelRepository;
        this.paymentRepository = paymentRepository;
        this.bookingRepository=bookingRepository;
    }
    @Override
    public Booking bookRoom(BookingDetailsDTO bookingDetailsDTO){

        Optional<Hotel> hotelOptional = hotelRepository.findById(bookingDetailsDTO.get_hotelId());
        if(hotelOptional.isEmpty()){
            throw new IllegalArgumentException("Hotel Not Found");
        }
        Hotel currentHotel = hotelOptional.get();
        List<Room> availableRooms = currentHotel.getRooms();
        Optional<Room> optionalRoom = availableRooms.stream()
                .filter(p -> p.getId().equals(bookingDetailsDTO.get_roomId())).findFirst();
        if(optionalRoom.isEmpty()){
            throw new IllegalArgumentException("Room not found");
        }
        Room currentRoom = optionalRoom.get();
        if(currentRoom.getRoomCount()<bookingDetailsDTO.getCustomerRoomCount()){
            throw new IllegalArgumentException("No available rooms");
        }
        Booking currentBooking = new Booking();
        currentRoom.setRoomCount((currentRoom.getRoomCount())- bookingDetailsDTO.getCustomerRoomCount());
        hotelRepository.save(currentHotel);
        currentBooking.set_userId(bookingDetailsDTO.get_customerId());
        currentBooking.set_hotelId(bookingDetailsDTO.get_hotelId());
        currentBooking.set_roomId(bookingDetailsDTO.get_roomId());
        currentBooking.setNumOfRooms(bookingDetailsDTO.getCustomerRoomCount());
        currentBooking.setNumOfDays(bookingDetailsDTO.getCustomerDayCount());
        currentBooking.setTotalAmount((bookingDetailsDTO.getCustomerRoomCount())*(bookingDetailsDTO.getCustomerDayCount())* currentRoom.getRoomRate());
        currentBooking.setGstOfTotalAmount((currentBooking.getTotalAmount()*12)/100);
        Booking booking = bookingRepository.save(currentBooking);
        Payment currentPayment = new Payment();
        currentPayment.set_userId(bookingDetailsDTO.get_customerId());
        currentPayment.set_hotelId(bookingDetailsDTO.get_hotelId());
        currentPayment.set_roomId(bookingDetailsDTO.get_roomId());
        currentPayment.setNumOfRooms(bookingDetailsDTO.getCustomerRoomCount());
        currentPayment.setNumOfDays(bookingDetailsDTO.getCustomerDayCount());
        currentPayment.set_bookingId(booking.getId());
        currentPayment.setPaymentStatus(false);
        paymentRepository.save(currentPayment);
        return booking;


    }

}
