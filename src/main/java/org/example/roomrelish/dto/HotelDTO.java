package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomrelish.models.Location;
import org.example.roomrelish.models.Room;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelDTO {
    private String hotelName;
    private String hotelType;
    private Location location;
    private int priceStartingFrom;
    private String overview;
    private List<String> locationFeatures;
    private List<String> amenities;
    private List<String> images;
    private List<Room> rooms;
    private int totalRooms;
}
