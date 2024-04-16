package org.example.carddetails.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.carddetails.dto.BookingDetailsDTO;
import org.example.carddetails.models.Booking;
import org.example.carddetails.models.Customer;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.models.Room;
import org.example.carddetails.repository.BookingRepository;
import org.example.carddetails.repository.CustomerRepository;
import org.example.carddetails.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;
@Service

public class BookingServiceImpl {
    @Autowired
    private final HotelRepository hotelRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    public BookingServiceImpl(HotelRepository hotelRepository,BookingRepository bookingRepository)
    {
        this.hotelRepository=hotelRepository;
        this.bookingRepository=bookingRepository;
    }
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
        currentBooking.setNumOfDays(bookingDetailsDTO.getCustomerRoomCount());
        currentBooking.setTotalAmount((bookingDetailsDTO.getCustomerRoomCount())*(bookingDetailsDTO.getCustomerDayCount())* currentRoom.getRoomRate());
        currentBooking.setGstOfTotalAmount((currentBooking.getTotalAmount()*12)/100);
        return bookingRepository.save(currentBooking);
    }

}
