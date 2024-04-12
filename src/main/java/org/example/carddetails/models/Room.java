package org.example.carddetails.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String id;
    private String roomType;
    private String roomSpecification;
    private int roomRate;
    private int roomCount;
}
