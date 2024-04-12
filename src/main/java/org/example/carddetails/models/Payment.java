package org.example.carddetails.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String _userId;
    private String _hotelId;
    private String _roomId;
    private int numOfRooms;
    private int numOfDays;
    private double totalAmount;
    private double gstOfTotalAmount;
}
