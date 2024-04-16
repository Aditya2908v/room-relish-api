package org.example.roomrelish.services;

import org.example.roomrelish.dto.CardDTO;
import org.example.roomrelish.dto.CustomerProfile;
import org.example.roomrelish.dto.UpdateCustomerDTO;
import org.example.roomrelish.models.Customer;
import org.example.roomrelish.models.Hotel;

import java.util.List;

public interface CustomerService {
    void addCardToUser(CardDTO cardDTO,String userEmail);

    List<Customer> getAllCustomers();

    boolean updateCustomer(String userEmail, UpdateCustomerDTO updateCustomerDTO);

    CustomerProfile getProfileInfo(String userEmail, String navbar);

    String getProfilePicture(String userEmail);

    boolean uploadImage(String userEmail, String type, String fileName);

    List<Hotel> getFavouriteHotels(String userEmail);

    void addFavouriteHotel(String userEmail, String hotelId);

    void deleteFavouriteHotel(String userEmail, String hotelId);

    List<Hotel> findRecentHotels(String userEmail);
}
