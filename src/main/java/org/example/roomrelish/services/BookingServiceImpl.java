package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Payment;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.example.roomrelish.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

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
        booking.setCheckInDate(bookingDetailsDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDetailsDTO.getCheckOutDate());
        booking.setTotalAmount(bookingDetailsDTO.getCustomerRoomCount() * bookingDetailsDTO.getCustomerDayCount() * currentRoom.getRoomRate());
        booking.setGstOfTotalAmount((booking.getTotalAmount() * 12) / 100);
        Payment payment = new Payment();
        // Save booking
        Booking booking1 = bookingRepository.save(booking);
        //Save in payment and set payment status false
        payment.set_bookingId(booking1.getId());
        payment.set_roomId(booking1.get_roomId());
        payment.set_hotelId(booking.get_hotelId());
        payment.set_userId(booking.get_userId());
        payment.setHotelName(currentHotel.getHotelName());
        payment.setRoomName(currentRoom.getRoomType());
        payment.setNumOfDays(booking.getNumOfDays());
        payment.setNumOfRooms(booking.getNumOfRooms());
        payment.setCheckInDate(booking.getCheckInDate());
        payment.setCheckOutDate(booking.getCheckOutDate());
        payment.setTotalAmount(booking1.getTotalAmount()+ booking1.getGstOfTotalAmount());
        String image = currentHotel.getImages().getFirst();
        payment.setHotelImage(image);
        payment.setPaymentStatus(false);
        paymentRepository.save(payment);
        return booking1;

    }
}
