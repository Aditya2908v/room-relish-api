package org.example.carddetails.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsDTO {
    private String _customerId;
    private String _hotelId;
    private String _roomId;
    private int customerRoomCount;
    private int customerDayCount;
}
