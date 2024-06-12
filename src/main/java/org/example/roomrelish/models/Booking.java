package org.example.roomrelish.models;

import lombok.*;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@TestOnly
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
@Builder
public class Booking {
    @Id
    private String id;
    private String _userId;
    private String _hotelId;
    private String _roomId;
    private int numOfRooms;
    private int numOfDays;
    private double totalAmount;
    private double gstOfTotalAmount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

}
