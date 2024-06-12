package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TestOnly
public class BookingDetailsDTO {
    private String _userId;
    private String _hotelId;
    private String _roomId;
    private int customerRoomCount;
    private int customerDayCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String roomType;
}

