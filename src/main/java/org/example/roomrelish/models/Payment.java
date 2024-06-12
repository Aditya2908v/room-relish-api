package org.example.roomrelish.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@TestOnly
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String _hotelId;
    private String hotelName;
    private String hotelImage;
    private String _userId;
    private String _roomId;
    private String roomName;
    private String _bookingId;
    private double totalAmount;
    private int numOfRooms;
    private int numOfDays;
    private boolean paymentStatus;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}