package org.example.carddetails.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "hotels")
public class Hotel {
    @Id
    private String id;
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
    private int totalRooms;
}

