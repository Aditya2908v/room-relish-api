package org.example.roomrelish.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
public class RoomDTO {
    private String roomType;
    private String roomSpecification;
    private int roomRate;
    private int roomCount;
}