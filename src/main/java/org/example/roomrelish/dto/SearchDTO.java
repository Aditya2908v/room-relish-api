package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {
    private String cityName;
    private Date checkInDate;
    private Date checkOutDate;
    private int countOfRooms;
    private int priceRangeMax;
    private int priceRangeMin;
    private double rating;
    private List<String> amenities;



}
