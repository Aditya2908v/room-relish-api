package org.example.roomrelish.services;

import lombok.RequiredArgsConstructor;
import org.example.roomrelish.models.Booking;
import org.example.roomrelish.models.Payment;
import org.example.roomrelish.repository.BookingRepository;
import org.example.roomrelish.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Payment confirmBook(String _bookingId){
        Optional<Payment> paymentOptional = paymentRepository.findBy_bookingId(_bookingId);
        if(paymentOptional.isEmpty()){
            throw new IllegalArgumentException();
        }
        Payment currentPayment = paymentOptional.get();
        currentPayment.setPaymentStatus(true);
        return paymentRepository.save(currentPayment);
    }

    @Override
    public List<Payment> getMyBookings(String _userId){
        List<Payment> paymentList = paymentRepository.findAllBy_userId(_userId);
        return paymentList;
    }

}
