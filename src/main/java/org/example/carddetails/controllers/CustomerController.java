package org.example.carddetails.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.carddetails.dto.*;
import org.example.carddetails.models.Customer;
import org.example.carddetails.repository.CustomerRepository;
import org.example.carddetails.services.AuthService;
import org.example.carddetails.services.CustomerService;
import org.example.carddetails.services.JwtService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @QueryMapping("users")
    public List<Customer> getAllCustomers_GraphQL() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(
            description = "User Registration",
            summary = "Registers a new Customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User Registration Successful"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request. The request body is invalid or missing required fields"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        try{
            AuthResponse response = authService.registerCustomer(request);
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(
            description = "User Authentication",
            summary = "Authenticates a Customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User Authentication Successful"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User not found or incorrect credentials."
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        try{
            AuthResponse response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


    @PostMapping("/addCard")
    public ResponseEntity<?> addCardToUser(@RequestBody CardDTO cardDTO, HttpServletRequest request) {
        try {
            String userEmail = extractUserEmailFromRequest(request);
            customerService.addCardToUser(cardDTO, userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //update user
    @Operation(
            description = "Update Customer Details",
            summary = "Updates Customer E-Mail, phone number and password",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer details updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Customer is not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error"
                    )
            }
    )
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateCustomerDTO updateCustomerDTO, HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            if(customerService.updateCustomer(userEmail, updateCustomerDTO)){
                return ResponseEntity.ok().body("Customer successfully updated");
            }
            return ResponseEntity.badRequest().body("Customer not found");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }
    //specifically for navbar/ to check weather the user is logged in
    @GetMapping("/navbar")
    public ResponseEntity<?> getNavbarDetails(HttpServletRequest request) {
        try {
            String userEmail = extractUserEmailFromRequest(request);
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
            }
            CustomerProfile customerProfile = customerService.getProfileInfo(userEmail, "navbar");
            if (customerProfile == null) {
                return ResponseEntity.badRequest().body("Customer not found");
            }
            return ResponseEntity.ok().body(new NavbarResponse(true, customerProfile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
    //get profile details for profile page
    @GetMapping("/profile-details")
    public ResponseEntity<?> getProfileDetails(HttpServletRequest request) {
        try{
            String userEmail = extractUserEmailFromRequest(request);
            CustomerProfile customerProfile = customerService.getProfileInfo(userEmail, "profile");
            if (customerProfile == null) {
                return ResponseEntity.badRequest().body("Customer not found");
            }
            return ResponseEntity.ok().body(customerProfile);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //get profile picture
    // add or update profile or cover picture
    // logout controller
    // to view list of favourite hotels
    // to add a hotel to favourite hotels
    // to remove a hotel from favourite hotels
    // recent searches
    // fetch user details by ID along with decrypted card details
    // function to add new card details to the added cards field
    // recent searches





    //utility function to get JWT token form the request
    private String extractUserEmailFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Unauthorized");
        }
        final String jwtToken = authHeader.substring(7);
        return jwtService.extractUsername(jwtToken);
    }
}
