package org.example.carddetails.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String roomType;
    private String roomSpecification;
    private int roomRate;
    private int roomCount;
}