package org.example.roomrelish.services;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoSocketException;
import lombok.RequiredArgsConstructor;
import org.example.roomrelish.ExceptionHandler.CustomDataAccessException;
import org.example.roomrelish.ExceptionHandler.CustomDuplicateBookingException;
import org.example.roomrelish.ExceptionHandler.CustomMongoSocketException;
import org.example.roomrelish.dto.BookingDetailsDTO;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Hotel;
import org.example.roomrelish.models.Payment;
import org.example.roomrelish.models.Room;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.HotelRepository;
import org.example.roomrelish.repository.PaymentRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl {

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;



    public Booking bookRoom(BookingDetailsDTO bookingDetailsDTO) throws CustomDataAccessException, CustomDuplicateBookingException, CustomMongoSocketException {
        if(bookingDetailsDTO==null){
            throw new NullPointerException("No details provided");
        }
        Optional<Hotel> hotelOptional = hotelRepository.findByHotelByIdAndRoomById(bookingDetailsDTO.get_hotelId(),bookingDetailsDTO.get_roomId());
        if (hotelOptional.isEmpty()) {
            throw new IllegalArgumentException("Hotel Not Found");
        }
        Hotel currentHotel = hotelOptional.get();

       Room currentRoom = currentHotel.getRooms().stream()
                .filter(room -> room.getId().equals(bookingDetailsDTO.get_roomId()))
                .findFirst().orElseThrow(()-> new IllegalArgumentException("Room not found"));

       Booking booking = createBooking(bookingDetailsDTO,currentRoom);
       Payment payment = createPayment(booking,currentRoom,currentHotel);

       return saveBookingAndPayment(booking,payment);

    }

    private Booking saveBookingAndPayment(Booking booking, Payment payment) throws CustomDuplicateBookingException, CustomDataAccessException, CustomMongoSocketException {
        try{
            Booking savedBooking = bookingRepository.save(booking);
            payment.set_bookingId(savedBooking.getId());
            paymentRepository.save(payment);
            return savedBooking;
        }
        catch(DuplicateKeyException e){
            throw new CustomDuplicateBookingException(e);
        }
        catch (DataAccessException e){
            throw new CustomDataAccessException(e);
        }
        catch(MongoSocketException e){
            throw new CustomMongoSocketException(e);
        }
    }

    private Payment createPaymentPrivate(Booking booking, Room currentRoom,Hotel currentHotel) {
        return Payment.builder()
                ._bookingId(booking.getId())
                ._userId(booking.get_userId())
                ._hotelId(booking.get_hotelId())
                ._roomId(booking.get_roomId())
                .hotelName(currentHotel.getHotelName())
                .roomName(currentRoom.getRoomType())
                .numOfDays(booking.getNumOfDays())
                .numOfRooms(booking.getNumOfRooms())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalAmount(booking.getTotalAmount()+ booking.getGstOfTotalAmount())
                .hotelImage(currentHotel.getImages().getFirst())
                .paymentStatus(false)
                .build();
    }

    public Payment createPayment(Booking booking, Room currentRoom, Hotel currentHotel){
        return createPaymentPrivate(booking,currentRoom,currentHotel);
    }

    private Booking createBookingPrivate(BookingDetailsDTO bookingDetailsDTO, Room currentRoom) {
        RoomSelection roomSelection = new RoomSelection();
        RoomTypeInterface roomTypeInterface = roomSelection.selectRoom(bookingDetailsDTO.getRoomType());
        double totalAmount = (bookingDetailsDTO.getCustomerRoomCount()*bookingDetailsDTO.getCustomerDayCount()*currentRoom.getRoomRate())+roomTypeInterface.roomServiceCharge();
        double gstOfTotalAmount = ((totalAmount * 12) /100);
        return Booking.builder()
                ._userId(bookingDetailsDTO.get_userId())
                ._hotelId(bookingDetailsDTO.get_hotelId())
                ._roomId(bookingDetailsDTO.get_roomId())
                .numOfRooms(bookingDetailsDTO.getCustomerRoomCount())
                .numOfDays(bookingDetailsDTO.getCustomerDayCount())
                .checkInDate(bookingDetailsDTO.getCheckInDate())
                .checkOutDate(bookingDetailsDTO.getCheckOutDate())
                .totalAmount(totalAmount)
                .gstOfTotalAmount(gstOfTotalAmount).build();

    }

    private void validateRoomAvailabilityPrivate(int roomCount, int customerRoomCount) {
        if (roomCount < customerRoomCount) {
            throw new IllegalArgumentException("No available rooms");
        }
    }

    public void validateRoomAvailability(int roomCount,int customerRoomCount){
        validateRoomAvailabilityPrivate(roomCount,customerRoomCount);
    }

    public Booking createBooking(BookingDetailsDTO bookingDetailsDTO, Room currentRoom){
        return createBookingPrivate(bookingDetailsDTO,currentRoom);
    }
}
