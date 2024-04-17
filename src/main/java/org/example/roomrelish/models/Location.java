package org.example.roomrelish.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@TestOnly
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String cityName;
    private double latitude;
    private double longitude;
    private String address;
}