package org.example.carddetails.services;

import org.example.carddetails.dto.CardDTO;
import org.example.carddetails.dto.CustomerProfile;
import org.example.carddetails.dto.UpdateCustomerDTO;
import org.example.carddetails.models.Customer;
import org.example.carddetails.models.Hotel;

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
