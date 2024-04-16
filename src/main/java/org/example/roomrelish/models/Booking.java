package org.example.roomrelish.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
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
    private Date checkInDate;
    private Date checkOutDate;

}
