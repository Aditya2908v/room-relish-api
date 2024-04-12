package org.example.carddetails.services;

import lombok.RequiredArgsConstructor;
import org.example.carddetails.dto.CardDTO;
import org.example.carddetails.dto.UpdateCustomerDTO;
import org.example.carddetails.models.Card;
import org.example.carddetails.models.Customer;
import org.example.carddetails.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public void addCardToUser(CardDTO cardDTO, String userEmail) {
        Customer customer = customerRepository.findByEmail(userEmail).orElse(null);
        if(customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        Card card = new Card();
        card.setCardNumber(passwordEncoder.encode(cardDTO.getCardNumber()));
        card.setCardHolder(cardDTO.getCardHolder());
        card.setExpirationDate(cardDTO.getExpirationDate());
        card.setCvv(passwordEncoder.encode(cardDTO.getCvv()));
        card.setCardName(cardDTO.getCardName());
        List<Card> cards = customer.getAddedCards();
        if(cards == null) {
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
            if(optionalCustomer.isEmpty()) {
                return false;
            }
            Customer customer = optionalCustomer.get();

            //check if the email is being updated and if it's unique
            if(updateCustomerDTO.getEmail() != null && !updateCustomerDTO.getEmail().equals(customer.getEmail())){
                Optional<Customer> existingCustomer = customerRepository.findByEmail(updateCustomerDTO.getEmail());
                if(existingCustomer.isPresent() && !existingCustomer.get().getId().equals(customer.getId())) {
                    return false; // email already in user
                }
                customer.setEmail(updateCustomerDTO.getEmail());
            }

            //check if phone number is being updated and if it's unique
            if(updateCustomerDTO.getPhoneNumber() != null && !updateCustomerDTO.getPhoneNumber().equals(customer.getPhoneNumber())){
                Optional<Customer> existingUserWithPhoneNumber = customerRepository.findByPhoneNumber(updateCustomerDTO.getPhoneNumber());
                if(existingUserWithPhoneNumber.isPresent() && !existingUserWithPhoneNumber.get().getId().equals(customer.getId())) {
                    return false; // phone number is already in use
                }
                customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
            }
            if(updateCustomerDTO.getPassword() != null && !updateCustomerDTO.getPassword().equals(customer.getPassword())){
                customer.setPassword(passwordEncoder.encode(updateCustomerDTO.getPassword()));
            }
            customerRepository.save(customer);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
