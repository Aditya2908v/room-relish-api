package org.example.carddetails.services;

import org.example.carddetails.dto.HotelDTO;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class HotelServiceImplTest {
    @Mock
    private HotelRepository hotelRepository;
    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createHotel_validHotelDTO_savedSuccessfully() {
        // Given
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Test Hotel");
        // Set other properties of hotelDTO

        Hotel savedHotel = new Hotel();
        savedHotel.setId("1");
        // Set other properties of savedHotel

        // Mock the behavior of hotelRepository.save
        when(hotelRepository.save(any())).thenReturn(savedHotel);

        // When
        Hotel result = hotelService.createHotel(hotelDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId()); // Assuming getId() returns the ID of the saved hotel
        verify(hotelRepository, times(1)).save(any());
    }
}