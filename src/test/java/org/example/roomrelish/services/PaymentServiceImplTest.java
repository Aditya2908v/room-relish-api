package org.example.roomrelish.services;

import org.example.roomrelish.models.Payment;
import org.example.roomrelish.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConfirmBook_Exception(){
        String _bookingId = "134567890";
        when(paymentRepository.findBy_bookingId(_bookingId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->paymentService.confirmBook(_bookingId));
        verify(paymentRepository,times(1)).findBy_bookingId(_bookingId);
        verifyNoMoreInteractions(paymentRepository);
}
}
