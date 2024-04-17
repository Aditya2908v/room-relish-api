package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TestOnly
public class SearchCriteria {
    private Integer minPrice;
    private Integer maxPrice;
    private Double rating;
    private List<String> amenities;
}