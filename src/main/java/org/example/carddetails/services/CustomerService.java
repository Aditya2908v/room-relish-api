package org.example.carddetails.services;

import org.example.carddetails.dto.CardDTO;
import org.example.carddetails.dto.CustomerProfile;
import org.example.carddetails.dto.UpdateCustomerDTO;
import org.example.carddetails.models.Customer;

import java.util.List;

public interface CustomerService {
    void addCardToUser(CardDTO cardDTO,String userEmail);

    List<Customer> getAllCustomers();

    boolean updateCustomer(String userEmail, UpdateCustomerDTO updateCustomerDTO);

    CustomerProfile getProfileInfo(String userEmail, String navbar);
}
