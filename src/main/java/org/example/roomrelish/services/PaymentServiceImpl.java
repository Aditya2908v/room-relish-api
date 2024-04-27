package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Payment;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.example.roomrelish.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final HotelRepository hotelRepository;

    @Override
    public Payment confirmBook(String _bookingId){
        Optional<Payment> paymentOptional = paymentRepository.findBy_bookingId(_bookingId);
        if(paymentOptional.isEmpty()){
            throw new IllegalArgumentException();
        }
        Payment currentPayment = paymentOptional.get();
        currentPayment.setPaymentStatus(true);
        Optional<Hotel> hotelOptional = hotelRepository.findById(currentPayment.get_hotelId());
        Hotel currentHotel = new Hotel();
        if (hotelOptional.isPresent()){
            currentHotel = hotelOptional.get();
        }
        Optional<Room> optionalRoom = currentHotel.getRooms().stream()
                .filter(room -> room.getId().equals(currentPayment.get_roomId()))
                .findFirst();
        Room currentRoom = new Room();
        if(optionalRoom.isPresent()){
            currentRoom = optionalRoom.get();
        }
        Optional<Booking> booking = bookingRepository.findById(_bookingId);
        Booking currentBooking = new Booking();
        if(booking.isPresent()){
            currentBooking=booking.get();
        }

        currentRoom.setRoomCount(currentRoom.getRoomCount() - currentBooking.getNumOfRooms());
        currentHotel.setTotalRooms(currentHotel.getTotalRooms()-currentBooking.getNumOfRooms());
        hotelRepository.save(currentHotel);


        return paymentRepository.save(currentPayment);
    }

    @Override
    public List<Payment> getMyBookings(String _userId){
        List<Payment> paymentList = paymentRepository.findAllBy_userId(_userId);
        return paymentList;
    }

    @Override
    public String deleteBooking(String _bookingId){
        System.out.println("Inside deleteBooking");
        Optional<Payment> payment = paymentRepository.findBy_bookingId(_bookingId);
        Optional<Booking> booking = bookingRepository.findById(_bookingId);
        if(payment.isEmpty()||booking.isEmpty()){
            throw  new IllegalArgumentException("No booking found");
        }
        Payment currentPayment = payment.get();
        Booking currentBooking = booking.get();
        if(currentPayment.isPaymentStatus()){
            Optional<Hotel> hotel = hotelRepository.findById(currentPayment.get_hotelId());
            Hotel currentHotel = new Hotel();
            if(hotel.isPresent()){
                currentHotel = hotel.get();
            }
            List<Room> rooms = currentHotel.getRooms();
            Optional<Room> optionalRoom = rooms.stream()
                    .filter(room -> room.getId().equals(currentPayment.get_roomId()))
                    .findFirst();
            Room currentRoom = new Room();
            if(optionalRoom.isPresent()){
                currentRoom = optionalRoom.get();
            }
            currentRoom.setRoomCount(currentRoom.getRoomCount() + currentBooking.getNumOfRooms());
            currentHotel.setRooms(rooms);
            currentHotel.setTotalRooms(currentHotel.getTotalRooms() + currentBooking.getNumOfRooms());
            hotelRepository.save(currentHotel);
            bookingRepository.delete(currentBooking);
            paymentRepository.delete(currentPayment);
            return "Cancelled booking";
        }
        else{
            bookingRepository.delete(currentBooking);
            paymentRepository.delete(currentPayment);
            return "Booking details deleted";
        }

    }

}
