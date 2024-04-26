package org.example.roomrelish.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TestOnly
public class BookingDetailsDTO {
    private String _customerId;
    private String _hotelId;
    private String _roomId;
    private int customerRoomCount;
    private int customerDayCount;
    private Date checkInDate;
    private Date checkOutDate;
}

