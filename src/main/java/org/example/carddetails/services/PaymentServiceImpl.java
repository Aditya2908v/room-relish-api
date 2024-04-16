package org.example.carddetails.services;

import org.example.carddetails.models.Booking;
import org.example.carddetails.models.Payment;
import org.example.carddetails.repository.BookingRepository;
import org.example.carddetails.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl {
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    public Payment confirmBook(Booking booking){
        Optional<Booking> bookingOptional = bookingRepository.findById(booking.getId());
        if(bookingOptional.isEmpty()){
            throw new IllegalArgumentException();
        }
        Payment currentPayment = new Payment();
        currentPayment.set_bookingId(booking.getId());
        currentPayment.set_hotelId(booking.get_hotelId());
        currentPayment.set_roomId(booking.get_roomId());
        currentPayment.set_userId(booking.get_userId());
        currentPayment.setNumOfDays(booking.getNumOfDays());
        currentPayment.setNumOfRooms(booking.getNumOfRooms());
        currentPayment.setPaymentStatus(true);
        return paymentRepository.save(currentPayment);
    }
}
