package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomrelish.models.GuestReview;
import org.example.roomrelish.models.Location;
import org.example.roomrelish.models.Room;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TestOnly
public class HotelDTO {
    private String hotelName;
    private String hotelType;
    private Location location;
    private double rating;
    private String overallReview;
    private int numReviews;
    private int priceStartingFrom;
    private String overview;
    private List<String> locationFeatures;
    private List<String> amenities;
    private List<String> images;
    private List<Room> rooms;
    private List<GuestReview> guestReviews;
}
