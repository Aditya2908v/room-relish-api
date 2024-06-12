package org.example.roomrelish.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.annotation.Id;

import java.util.List;

@TestOnly
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    private String id;
    private String roomType;
    private String roomSpecification;
    private int roomRate;
    private int roomCountBasic;
    private List<RoomAvailability> roomAvailabilityList;
}
