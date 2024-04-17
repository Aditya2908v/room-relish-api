package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
public class ReviewDTO {
    private String userid;
    private double rating;
    private String comment;
}
