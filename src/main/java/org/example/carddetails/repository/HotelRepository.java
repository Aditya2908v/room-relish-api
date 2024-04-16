package org.example.carddetails.repository;

import org.example.carddetails.models.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

public interface HotelRepository extends MongoRepository<Hotel, String> {
    /*@Query("{ " +
            "'location.cityName': { $regex: ?0, $options: 'i' }, " + // City name
            "'availability.rooms': { $gte: ?1 }, " + // Rooms
            "'rating': { $gte: ?2 }, " + // Rating
            "'amenities': { $all: ?3 } " + // Amenities
            "}")*/
    //@Query("{ 'location.cityName': { $regex: ?0, $options: 'i' }, 'availability.rooms': { $gte: ?1 } }")
    //List<Hotel> findByLocationCityName(String cityName);
    List<Hotel> findByLocationCityNameAndRatingGreaterThanEqual(
            String location_cityName, double rating);


    List<Hotel> findByLocationCityName(String cityName);
}
