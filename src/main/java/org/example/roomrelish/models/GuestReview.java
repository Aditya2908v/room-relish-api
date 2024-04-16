package org.example.roomrelish.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestReview {
    @Id
    private String _id = UUID.randomUUID().toString();
    private String user;
    private double guestRating;
    private String comment;
}