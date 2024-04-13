package org.example.carddetails.services;

import lombok.RequiredArgsConstructor;
import org.example.carddetails.dto.CardDTO;
import org.example.carddetails.dto.CustomerProfile;
import org.example.carddetails.dto.UpdateCustomerDTO;
import org.example.carddetails.models.Card;
import org.example.carddetails.models.Customer;
import org.example.carddetails.models.Hotel;
import org.example.carddetails.repository.CustomerRepository;
import org.example.carddetails.repository.HotelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final HotelRepository hotelRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    //TODO add code to encrypt the card details before storing
    @Override
    public void addCardToUser(CardDTO cardDTO, String userEmail) {
        Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardHolder(cardDTO.getCardHolder());
        card.setExpirationDate(cardDTO.getExpirationDate());
        card.setCvv(cardDTO.getCvv());
        card.setCardName(cardDTO.getCardName());
        List<Card> cards = customer.getAddedCards();
        if (cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
        customer.setAddedCards(cards);
        customerRepository.save(customer);
    }


    @Override
    public boolean updateCustomer(String userEmail, UpdateCustomerDTO updateCustomerDTO) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(userEmail);
            if (optionalCustomer.isEmpty()) {
                return false;
            }
            Customer customer = optionalCustomer.get();

            //check if the email is being updated and if it's unique
            if (updateCustomerDTO.getEmail() != null && !updateCustomerDTO.getEmail().equals(customer.getEmail())) {
                Optional<Customer> existingCustomer = customerRepository.findByEmail(updateCustomerDTO.getEmail());
                if (existingCustomer.isPresent() && !existingCustomer.get().getId().equals(customer.getId())) {
                    return false; // email already in user
                }
                customer.setEmail(updateCustomerDTO.getEmail());
            }

            //check if phone number is being updated and if it's unique
            if (updateCustomerDTO.getPhoneNumber() != null && !updateCustomerDTO.getPhoneNumber().equals(customer.getPhoneNumber())) {
                Optional<Customer> existingUserWithPhoneNumber = customerRepository.findByPhoneNumber(updateCustomerDTO.getPhoneNumber());
                if (existingUserWithPhoneNumber.isPresent() && !existingUserWithPhoneNumber.get().getId().equals(customer.getId())) {
                    return false; // phone number is already in use
                }
                customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
            }
            if (updateCustomerDTO.getPassword() != null && !updateCustomerDTO.getPassword().equals(customer.getPassword())) {
                customer.setPassword(passwordEncoder.encode(updateCustomerDTO.getPassword()));
            }
            customerRepository.save(customer);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public CustomerProfile getProfileInfo(String userEmail, String detailsFor) {
        Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        CustomerProfile customerProfile = new CustomerProfile();
        if (detailsFor != null && detailsFor.equals("navbar")) {
            customerProfile.setId(customer.getId());
            customerProfile.setUsername(customer.getUsername());
            customerProfile.setProfilePicture(customer.getProfilePicture());
        } else {
            customerProfile.setId(customer.getId());
            customerProfile.setUsername(customer.getUsername());
            customerProfile.setEmail(customer.getEmail());
            customerProfile.setPhoneNumber(customer.getPhoneNumber());
            customerProfile.setPassword(customer.getPassword());
            customerProfile.setDob(customer.getDateOfBirth());
            customerProfile.setAddress(customer.getAddress());
        }
        return customerProfile;
    }

    @Override
    public String getProfilePicture(String userEmail) {
        Optional<Customer> customer = customerRepository.findByEmail(userEmail);
        return customer.map(Customer::getProfilePicture).orElse(null);
    }

    //TODO change the file path correctly
    @Override
    public boolean uploadImage(String userEmail, String type, String fileName) {
        try {
            Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found");
            }
            if (type.equals("profile")) {
                customer.setProfilePicture("http://localhost:8081/profiles/" + fileName);
            } else {
                customer.setCoverPicture("http://localhost:8081/profiles/" + fileName);
            }
            customerRepository.save(customer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Hotel> getFavouriteHotels(String userEmail) {
        try {
            Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found");
            }
            List<String> hotelIds = customer.getFavouriteHotels();
            return hotelIds.stream()
                    .map(hotelRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addFavouriteHotel(String userEmail, String hotelId) {
        Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        List<String> favouriteHotelIds = customer.getFavouriteHotels();
        if (!favouriteHotelIds.contains(hotelId)) {
            favouriteHotelIds.add(hotelId);
            customer.setFavouriteHotels(favouriteHotelIds);
            customerRepository.save(customer);
        }
    }

    @Override
    public void deleteFavouriteHotel(String userEmail, String hotelId) {
        Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        List<String> favouriteHotelIds = customer.getFavouriteHotels();
        if (!favouriteHotelIds.isEmpty() && favouriteHotelIds.contains(hotelId)) {
            favouriteHotelIds.remove(hotelId);
            customer.setFavouriteHotels(favouriteHotelIds);
            customerRepository.save(customer);
        }
    }

    @Override
    public List<Hotel> findRecentHotels(String userEmail) {
        Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        List<String> recentHotelIds = customer.getRecentVisitsOfHotels();
        Collections.reverse(recentHotelIds);
        return recentHotelIds.stream()
                .map(hotelRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
