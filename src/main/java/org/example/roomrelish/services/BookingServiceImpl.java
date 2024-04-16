package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    public Booking bookRoom(BookingDetailsDTO bookingDetailsDTO) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(bookingDetailsDTO.get_hotelId());
        if (hotelOptional.isEmpty()) {
            throw new IllegalArgumentException("Hotel Not Found");
        }
        Hotel currentHotel = hotelOptional.get();

        Optional<Room> optionalRoom = currentHotel.getRooms().stream()
                .filter(room -> room.getId().equals(bookingDetailsDTO.get_roomId()))
                .findFirst();
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }
        Room currentRoom = optionalRoom.get();

        if (currentRoom.getRoomCount() < bookingDetailsDTO.getCustomerRoomCount()) {
            throw new IllegalArgumentException("No available rooms");
        }

        // Decrease available room count
        currentRoom.setRoomCount(currentRoom.getRoomCount() - bookingDetailsDTO.getCustomerRoomCount());
        hotelRepository.save(currentHotel);

        // Create booking
        Booking booking = new Booking();
        booking.set_userId(bookingDetailsDTO.get_customerId());
        booking.set_hotelId(bookingDetailsDTO.get_hotelId());
        booking.set_roomId(bookingDetailsDTO.get_roomId());
        booking.setNumOfRooms(bookingDetailsDTO.getCustomerRoomCount());
        booking.setNumOfDays(bookingDetailsDTO.getCustomerDayCount());
        booking.setTotalAmount(bookingDetailsDTO.getCustomerRoomCount() * bookingDetailsDTO.getCustomerDayCount() * currentRoom.getRoomRate());
        booking.setGstOfTotalAmount((booking.getTotalAmount() * 12) / 100);

        // Save booking
        return bookingRepository.save(booking);
    }
}
