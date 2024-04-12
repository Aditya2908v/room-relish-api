package org.example.carddetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private Integer minPrice;
    private Integer maxPrice;
    private Double rating;
    private List<String> amenities;
}