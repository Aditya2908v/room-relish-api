package org.example.carddetails.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String _hotelId;
    private String _userId;
    private String _roomId;
    private String _paymentId;
    private int numOfRooms;
    private int numOfDays;
    private boolean paymentStatus;
}